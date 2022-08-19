package todoapp.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import todoapp.web.model.SiteProperties;

@Controller
public class LoginController {
	
	private final SiteProperties siteProperties;
	private final Logger logger = LoggerFactory.getLogger(getClass()); 
	
	public LoginController(SiteProperties siteProperties) {
		this.siteProperties = siteProperties;
	}
	
	/*
	@ModelAttribute("site")
	public SiteProperties siteProperties() {
		return siteProperties;
	}
	*/

	@GetMapping("/login")
	public void loginForm() {
	
	}
	
	@PostMapping("/login")
	//Servlet API로 클라이언트의 값 가져오기
	public void loginProcess(HttpServletRequest request) {
		//서버로 사용자의 입력 값을 가져온다.
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		logger.debug("login command: {}, {},", username, password);
	}
	
}
