package todoapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import todoapp.web.model.SiteProperties;

@Controller
public class LoginController {
	
	private final SiteProperties siteProperties;
	
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
	public void login() {
	}
	
}
