package todoapp.security.web.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import todoapp.security.AccessDeniedException;
import todoapp.security.UnauthorizedAccessException;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;
import todoapp.security.support.RolesAllowedSupport;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Role(역할) 기반으로 사용자가 사용 권한을 확인하는 인터셉터 구현체
 *
 * @author springrunner.kr@gmail.com
 */
public class RolesVerifyHandlerInterceptor implements HandlerInterceptor, RolesAllowedSupport {

	private final UserSessionRepository userSessionRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    public RolesVerifyHandlerInterceptor(UserSessionRepository userSessionRepository) {
    	this.userSessionRepository = userSessionRepository;
    }

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	//Servlet 스펙으로 좀 유저 권한을 확인 할 수 있는 매서드
    	//request.getUserPrincipal()
    	//request.isUserInRole(null)
    	//핸들러가 어떤 타입인가/
    	if(handler instanceof HandlerMethod) {
    		//이 핸들어에 RolesAllowed 애노테이션이 선언되어 있는지 확인후, 가져오기
    		RolesAllowed rolesAllowed = ((HandlerMethod) handler).getMethodAnnotation(RolesAllowed.class);
    		//핸들러의 RolesAllowed를 꺼냈는데 없다면 / 스프링의 애노테이션 유틸리티를 이용
    		if(Objects.isNull(rolesAllowed)) {
    			rolesAllowed = AnnotatedElementUtils.findMergedAnnotation(((HandlerMethod) handler).getBeanType(), RolesAllowed.class);
    		}
    		
    		if(Objects.nonNull(rolesAllowed)) {
    			log.debug("verify roles-allowed: {}", rolesAllowed);
    			//1.로그인이 되어 있는지?
    			//UserSession userSession = userSessionRepository.get();
    			//if(Objects.isNull(userSession)) {
				if(Objects.isNull(request.getUserPrincipal())) {//현재 사용자의 정보를 가져온다.
    				//로그인이 안되어 있으면, 예외 발생
    				throw new UnauthorizedAccessException();
    			}
    			//2. 역할은 적잘한가요?
    			//이 API를 실행할 권한이 있나 없나 확인
    			//RolesAllowed Annotation이 가지고 있는 모든 역할을 꺼내서 그 역할이 UserSession이 가지고 있는지를 보고
    			//UserSession이 가지고 있는 역할만 필터링에서 꺼낸다.
    			Set<String> matchedRoles = Stream.of(rolesAllowed.value())
    				//.filter(role -> userSession.hasRole(role))
    				.filter(role -> request.isUserInRole(role))
    				.collect(Collectors.toSet());
    			
    			log.debug("matchedRoles roles {}", matchedRoles);
    			if(matchedRoles.isEmpty()) {
    				throw new AccessDeniedException();
    			}
    			//역할이 적합하지 않다면, 예외를 발생시킬 예정
    			
    			//로그인이 되어 있다면, 핸들러 실행
    		}
    	}
    	return true;
    }

}