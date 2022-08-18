package todoapp.commons.web.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import todoapp.core.todos.domain.TodoEntityNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * 스프링부트에 기본 구현체인 {@link DefaultErrorAttributes}에 message 속성을 덮어쓰기 할 목적으로 작성한 컴포넌트이다.
 *
 * DefaultErrorAttributes는 message 속성을 예외 객체의 값을 사용하기 때문에 사용자가 읽기에 좋은 문구가 아니다.
 * 해당 메시지를 보다 읽기 좋은 문구로 가공해서 제공한다.
 *
 * @author springrunner.kr@gmail.com
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReadableErrorAttributes implements ErrorAttributes, HandlerExceptionResolver, Ordered {

    private final DefaultErrorAttributes delegate = new DefaultErrorAttributes();
    private final Logger log = LoggerFactory.getLogger(ReadableErrorAttributes.class);

    private final Environment environment;
    
    public ReadableErrorAttributes(Environment environment) {
    	this.environment = environment;
    }
    
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> attributes = delegate.getErrorAttributes(webRequest, options);
        Throwable error = getError(webRequest);

        log.debug("errorAttributes: {}, error: {}", attributes, error);
        // attributes, error 을 사용해 message 속성을 읽기 좋은 문구로 가공한다.
        if (Objects.nonNull(error)) {
        	// Exception.TodoEntityNotFoundException
        	// Exception.MethodArgumentNotValidException
        	
        	//분기문을 이용하지 않고, 규칙으로 정의해서 처리하기 
        	String errorCode = String.format("Exception.%s", error.getClass().getSimpleName());
        	String errorMessage = environment.getProperty(errorCode, error.getMessage());
        	
        	attributes.put("message", errorMessage);
        	
        	/*
        	if(error instanceof TodoEntityNotFoundException) {
        		attributes.put("message", environment.getProperty("Exception.TodoEntityNotFoundException"));
        	} else if(error instanceof MethodArgumentNotValidException) {
        		attributes.put("message", environment.getProperty("Exception.MethodArgumentNotValidException"));
        	}
        	*/
        }

        return attributes;
    }

    @Override
    public Throwable getError(WebRequest webRequest) {
        return delegate.getError(webRequest);
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception error) {
        return delegate.resolveException(request, response, handler, error);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


    /**
     * 예외 개체에서 {@link org.springframework.boot.context.properties.bind.BindResult}를 추출합니다.
     * 없으면 {@literal null}을 반환합니다.
     */
    static BindingResult extractBindingResult(Throwable error) {
        if (error instanceof BindingResult) {
            return (BindingResult) error;
        }
        if (error instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) error).getBindingResult();
        }
        return null;
    }

}
