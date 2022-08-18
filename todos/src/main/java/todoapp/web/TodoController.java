package todoapp.web;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractView;

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

	//produces 는 헤더의 Accept를 이용하여 결정한다.
	@RequestMapping(path = "/todos", produces = "text/csv")
	public void downloadTodos() {
		
	}
	
	//전문화된 형식의 ViewResolver와 뷰 생성
	public static class TodoCsvViewResolver implements ViewResolver{

		@Override
		public View resolveViewName(String viewName, Locale locale) throws Exception {
			if("todos".equals(viewName)) {
				return new TodoCsvView();
			}
			return null;
		}
		
		public static class TodoCsvView extends AbstractView implements View{

			final Logger logger = LoggerFactory.getLogger(getClass());
			
			public TodoCsvView() {
				setContentType("text/csv");
			}
			
			@Override
			protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
					HttpServletResponse response) throws Exception {
				logger.info("render model as csv content");
			}
			
		}
	}
	
}