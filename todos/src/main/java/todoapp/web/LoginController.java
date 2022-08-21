package todoapp.web;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import todoapp.core.user.application.UserPasswordVerifier;
import todoapp.core.user.application.UserRegistration;
import todoapp.core.user.domain.User;
import todoapp.core.user.domain.UserEntityNotFoundException;
import todoapp.core.user.domain.UserPasswordNotMatchedException;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;
import todoapp.web.model.SiteProperties;

@Controller
//@SessionAttributes("user") //유지할 모델의 키는 Session 수전에서 유지한다.
public class LoginController {
	
	private final UserPasswordVerifier userPasswordVerifier;
	private final UserRegistration userRegistration;
	private final UserSessionRepository userSessionRepository;
	private final SiteProperties siteProperties;
	private final Logger logger = LoggerFactory.getLogger(getClass()); 
	
	public LoginController(
			UserPasswordVerifier userPasswordVerifier, 
			UserRegistration userRegistration, 
			UserSessionRepository userSessionRepository,
			SiteProperties siteProperties) {
		this.userPasswordVerifier = userPasswordVerifier;
		this.userRegistration = userRegistration;
		this.userSessionRepository = userSessionRepository;
		this.siteProperties = siteProperties;
	}
	
	/*
	@ModelAttribute("site")
	public SiteProperties siteProperties() {
		return siteProperties;
	}
	*/

	@GetMapping("/login")
	public String loginForm() {
		if(Objects.nonNull(userSessionRepository.get())) {
			return "redirect:/todos";
		}
		return "login";
	}
	
	@PostMapping("/login")
	//Servlet API로 클라이언트의 값 가져오기
	//LoginCommand 클래스의 @Size의 검증을 이해하기 위해서 @Valid을 붙여준다.
	public String loginProcess(@Valid LoginCommand command, BindingResult bindingResult, Model model) {
		logger.debug("login command: {}", command);
		
		//0. 입력 값 검증에 실패한 경우: 로그인 페이지로 돌려보내기
		if(bindingResult.hasErrors()) {
			model.addAttribute("bindingResult", bindingResult);
			model.addAttribute("message", "입력 값이 없거나 올바르지 않아요.");
			return "login";//로그인 페이지를 뷰로 사용하겠다. 라는 의미
		}
		
		User user;
		
		try {
			//1. 사용자 저장소에 사용자가 있을 경우: 비밀번호 확인 후 로그인 처리
			user = userPasswordVerifier.verify(command.getUsername() , command.getPassword());
		} catch(UserEntityNotFoundException error) {
			//2. 사용자가 없는 경우: 회원가입 처리 후 로그인 처리
			user = userRegistration.join(command.getUsername() , command.getPassword());
		} 
		/*
		catch(UserPasswordNotMatchedException error){
			//3. 비밀번호가 틀린 경우: 로그인 페이지로 돌려보내기
			model.addAttribute("message", error.getMessage());
			return "login";
		}
		*/
		
		//담아준 user 객체를 세션에 넣어준다.
		//session.setAttribute("user", user);
		//@SessionAttributes("user") 모델의 키는 Session 수전에서 유지한다.
		//model.addAttribute("user", user); 
		userSessionRepository.set(new UserSession(user));
		
		return "redirect:/todos";
	}
	
	//컨트롤러에서 발생하는 에러가 아닌 직접 에러를 설정할 수 있다.(현재의 class 안에서만 유효)
	@ExceptionHandler(BindException.class)
	public String handleBindException(BindException error, Model model) {
		model.addAttribute("bindingResult", error.getBindingResult());
		model.addAttribute("message", "입력 값이 없거나 올바르지 않아요.");
		return "login";//로그인 페이지를 뷰로 사용하겠다. 라는 의미
	}
	
	//비밀번호 예외 에러 핸들러
	@ExceptionHandler(UserPasswordNotMatchedException.class)
		public String handlerUserPasswordNotMatchedException(UserPasswordNotMatchedException error, Model model) {
			model.addAttribute("message", error.getMessage());
			return "login";
	}
	
	static class LoginCommand{
		
		@Size(min = 4, max =20)
		String username;
		String password;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
		@Override
		public String toString() {
			return "LoginComman [username=" + username + ", password=" + password + "]";
		}
		
	}
	
}
