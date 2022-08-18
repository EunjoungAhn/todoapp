package todoapp.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import todoapp.core.todos.application.TodoEditor;
import todoapp.core.todos.application.TodoFinder;
import todoapp.core.todos.domain.Todo;

@RestController
@RequestMapping("/api/todos") //클래스 레벨에서 @RequestMapping을 하면 상속 관계 처럼 동작한다.
public class TodoRestController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private final TodoFinder finder;
	private final TodoEditor editor;
	
	public TodoRestController(TodoFinder finder, TodoEditor editor) {
		this.finder = finder;
		this.editor = editor;
	}

	@GetMapping
	public List<Todo> list(){
		return finder.getAll();
	}
	
	//등록 핸들러
	@PostMapping //서버로 보낸 요청 정보를 @RequestBody로 받기
	@ResponseStatus(HttpStatus.CREATED) // 핸들러가 정상적으로 잘 수행이 됐을때 보내는 201값
	public void create(@RequestBody @Valid WriteTodoCommand command) {
		logger.debug("requeest command: {}", command);
		
		editor.create(command.getTitle());
	}
	
	//수정 핸들러
	@PutMapping("/{id}") // 문자열 타입을 롱 타입으로 변경하는 작업이 일어난다.
	public void update(@PathVariable("id") Long id, @RequestBody @Valid WriteTodoCommand command) {
		logger.debug("request update id: {}, command: {}", id, command);
		
		editor.update(id, command.getTitle(), command.isCompleted());
	}
	
	//삭제 핸들러
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") Long id) {
		logger.debug("request delete id: {}", id);
		
		editor.delete(id);
	}
	
	//추후 서버로 부터 웹 요청정보를 입력 받는 데이터가 json에서 xml 또는 문자열 값을 받을때
	//어떤 특정 자료구조 형식 또는 맵 형식 말고 좀 더 구체적으로 명시해 주는 것이 좋다. 
	//게터와 세터를 가진 자바 빈으로써 
	//자바 빈에 관련된 객체를 command라고 부른다.
	static class WriteTodoCommand {
		
		@NotBlank
		@Size(min =4, max = 140)
		private String title;
		private boolean completed;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
		
		public boolean isCompleted() {
			return completed;
		}

		public void setCompleted(boolean completed) {
			this.completed = completed;
		}

		@Override
		public String toString() {
			return String.format("[title=%s, completed=%s]", title, completed);
		}
		
	}
}
