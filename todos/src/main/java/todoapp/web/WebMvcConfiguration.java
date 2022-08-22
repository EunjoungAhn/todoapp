package todoapp.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ObjectToStringHttpMessageConverter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import todoapp.commons.web.servlet.ExecutionTimeHandlerInterceptor;
import todoapp.commons.web.servlet.LoggingHandlerInterceptor;
import todoapp.commons.web.view.CommaSeparatedValuesView;
import todoapp.core.todos.domain.Todo;
import todoapp.security.UserSessionRepository;
import todoapp.security.web.servlet.RolesVerifyHandlerInterceptor;
import todoapp.security.web.servlet.UserSessionFilter;
import todoapp.security.web.servlet.UserSessionHandlerMethodArgumentResolver;
import todoapp.web.TodoController.TodoCsvViewResolver.TodoCsvView;

/**
 * Spring Web MVC 설정
 *
 * @author springrunner.kr@gmail.com
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
	
	@Autowired
	private UserSessionRepository userSessionRepository;
	
    @Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    	resolvers.add(new UserSessionHandlerMethodArgumentResolver(userSessionRepository));
    }
    
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//핸들러는 추가하는 순서대로 동작한다.
		registry.addInterceptor(new LoggingHandlerInterceptor());
		registry.addInterceptor(new ExecutionTimeHandlerInterceptor());
		registry.addInterceptor(new RolesVerifyHandlerInterceptor(userSessionRepository));
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	// 리소스 핸들러를 등록해서 정적 자원을 처리할 수 있다.
    	
    	//registry.addResourceHandler("/assets/**").addResourceLocations("classpath:assets/");
    	
    	//여러 자원도 한번에 등록 가능하다.
    	/*
    	registry.addResourceHandler("/assets/**")
    	.addResourceLocations("assets/", "classpath:assets/");
    	*/
	}

	@Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
		//스프링 WebMvcConfigurer 인터페이스의 viewResolver 사용
		//registry.viewResolver(new TodoController.TodoCsvViewResolver());
		
        //registry.enableContentNegotiation(new CommaSeparatedValuesView());
        // 위와 같이 직접 설정하면, 스프링부트가 구성한 ContentNegotiatingViewResolver 전략이 무시된다.
    }
	
	//새로운 메시지 컨버터 등록
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		DefaultConversionService conversionService = new DefaultConversionService();
		//왠만하면 DefaultConversionService로 마무리가 되지만
		//Todo 객체를 스트링으로 변환해 줄 수 있는 특별한 컨버터가 더 필요하다.
		conversionService.addConverter(new Converter<Todo, String>() {

			@Override
			public String convert(Todo source) {
				return source.toString();
			}
		});
		
		converters.add(new ObjectToStringHttpMessageConverter(conversionService));
	}
	
	
	@Bean
	public FilterRegistrationBean<CommonsRequestLoggingFilter> CommonsRequestLoggingFilter(){
		CommonsRequestLoggingFilter commonsRequestLoggingFilter = new CommonsRequestLoggingFilter();
		commonsRequestLoggingFilter.setIncludeHeaders(true);
		commonsRequestLoggingFilter.setIncludePayload(true);
		commonsRequestLoggingFilter.setIncludeClientInfo(true);
		
		FilterRegistrationBean<CommonsRequestLoggingFilter> filter = new FilterRegistrationBean<>();
		filter.setFilter(new CommonsRequestLoggingFilter());
		filter.setUrlPatterns(Collections.singletonList("/*")); //모든 요청 Url처리
		
		return filter;
	}
	
	@Bean
	public FilterRegistrationBean<UserSessionFilter> userSessionFilter(){
		UserSessionFilter userSessionFilter = new UserSessionFilter(userSessionRepository);
		
		FilterRegistrationBean<UserSessionFilter> filter = new FilterRegistrationBean<UserSessionFilter>();
		filter.setFilter(userSessionFilter);
		filter.setUrlPatterns(Collections.singleton("/*"));
		
		return filter;
	}
	
	/*
	//새로운 뷰 적용
	@Bean(name = "todos")
	public CommaSeparatedValuesView todoCsvView() {
		return new CommaSeparatedValuesView();
	}
	 */

	/**
     * 스프링부트가 생성한 ContentNegotiatingViewResolver를 조작할 목적으로 작성된 컴포넌트
     */
	@Configuration
    public static class ContentNegotiationCustomizer {
		
		@Autowired
        public void configurer(ContentNegotiatingViewResolver viewResolver) {
			List<View> defaultViews = new ArrayList<>(viewResolver.getDefaultViews());
			defaultViews.add(new CommaSeparatedValuesView());
			
			viewResolver.setDefaultViews(defaultViews);
		}

    }

}
