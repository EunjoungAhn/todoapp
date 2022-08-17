package todoapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import todoapp.web.model.SiteProperties;

@Controller
public class TodoController {
	
	private final SiteProperties siteProperties;
	
	public TodoController(SiteProperties siteProperties) {
		this.siteProperties = siteProperties;
	}
	
	@ModelAttribute("site")
	public SiteProperties siProperties() {
		return siteProperties;
	}
	
	//웹 요청을 처리하기 위한 핸들러를 작성
	@RequestMapping("/todos")
	public void todos() throws Exception {
	}
	
}
 