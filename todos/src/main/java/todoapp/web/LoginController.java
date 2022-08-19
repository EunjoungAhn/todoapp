package todoapp.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public void loginProcess(LoginCommand command) {
		logger.debug("login command: {}", command);
	}
	
	static class LoginCommand{
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
