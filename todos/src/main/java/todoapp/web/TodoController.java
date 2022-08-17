package todoapp.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import todoapp.web.model.SiteProperties;

@Controller
public class TodoController {
	
	private Environment environment;
	private String siteAuthor;
	
	/*생성자를 통해서 의존 관계 주입 후, 외부 설정값 가지고 오기
	public TodoController(Environment environment) {
		this.environment = environment;
	}
	*/
	
	//애노테이션으로 설정값 가져오기
	public TodoController(Environment environment, @Value("${site.author}") String sitAuthor) {
		this.environment = environment;
		this.siteAuthor = sitAuthor;
	}
	
	//웹 요청을 처리하기 위한 핸들러를 작성
	@RequestMapping("/todos")
	public ModelAndView todos() throws Exception {
		SiteProperties site = new SiteProperties();
		//site.setAuthor(environment.getProperty("site.author"));
		site.setAuthor(siteAuthor);
		site.setDescription("스프링 MVC 할 일 관리 앱");
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("site", site);
		mav.setViewName("todos");
		
		//ViewResolver viewResolver = new InternalResourceViewResolver();
		//View view = viewResolver.resolveViewName("todos", null);
		
		// ViewResolver의 규현체들은 대부분 접두사와 접미사라는 형식의 설정을 가질 수 있다.
		// fullViewName = prefix + "todos" + suffix
		//ex)
		//viewNmae = "todos"
		// prefix => classpath:/templates/
		// suffix => .html
		// fullViewName = classpath:/templates/todos.html
		
		return mav;
	}
	
}
