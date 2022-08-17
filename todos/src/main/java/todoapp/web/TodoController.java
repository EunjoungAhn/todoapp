package todoapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Controller
public class TodoController {
	
	//웹 요청을 처리하기 위한 핸들러를 작성
	@RequestMapping("/todos")
	public ModelAndView todos() throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("todos");
		
		ViewResolver viewResolver = new InternalResourceViewResolver();
		View view = viewResolver.resolveViewName("todos", null);
		
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
