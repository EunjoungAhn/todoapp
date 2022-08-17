package todoapp.web;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import todoapp.core.todos.application.TodoEditor;
import todoapp.core.todos.application.TodoFinder;
import todoapp.core.todos.domain.Todo;

@RestController
public class TodoRestController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private final TodoFinder finder;
	private final TodoEditor editor;
	
	public TodoRestController(TodoFinder finder, TodoEditor editor) {
		this.finder = finder;
		this.editor = editor;
	}

	@GetMapping("/api/todos")
	public List<Todo> list(){
		return finder.getAll();
	}
	
	@PostMapping("/api/todos") //서버로 보낸 요청 정보를 @RequestBody로 받기
	@ResponseStatus(HttpStatus.CREATED) // 핸들러가 정상적으로 잘 수행이 됐을때 보내는 201값
	public void create(@RequestBody CreateTodoCommand command) {
		logger.debug("requeest command: {}", command);
		
		editor.create(command.getTitle());
	}
	
	//추후 서버로 부터 웹 요청정보를 입력 받는 데이터가 json에서 xml 또는 문자열 값을 받을때
	//어떤 특정 자료구조 형식 또는 맵 형식 말고 좀 더 구체적으로 명시해 주는 것이 좋다. 
	//게터와 세터를 가진 자바 빈으로써 
	//자바 빈에 관련된 객체를 command라고 부른다.
	static class CreateTodoCommand {
		
		private String title;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		@Override
		public String toString() {
			return String.format("[title=%s]", title);
		}
		
	}
}
