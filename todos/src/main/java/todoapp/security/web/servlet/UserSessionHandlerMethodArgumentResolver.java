package todoapp.security.web.servlet;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;

public class UserSessionHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver{

	private final UserSessionRepository userSessionRepository;
	
	public UserSessionHandlerMethodArgumentResolver(UserSessionRepository userSessionRepository) {
		this.userSessionRepository = userSessionRepository;
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		//파라미터의 타입이 UserSession Class로부터 파생되었는지 확인
		return UserSession.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		//로그인된 세션 정보를 돌려주기
		return userSessionRepository.get();
	}

}
