package todoapp.web;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import todoapp.core.todos.application.TodoFinder;
import todoapp.core.todos.domain.Todo;

@RestController
public class TodoRestController {
	
	private final TodoFinder finder;
	
	public TodoRestController(TodoFinder finder) {
		this.finder = finder;
	}

	@RequestMapping("/api/todos")
	public List<Todo> list(){
		return finder.getAll();
	}
}
