package todoapp.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import todoapp.core.todos.application.TodoFinder;
import todoapp.core.todos.domain.Todo;

@RestController
public class TodoRestController {
	
	private final TodoFinder finder;
	
	public TodoRestController(TodoFinder finder) {
		this.finder = finder;
	}

	//@RequestMapping(path = "/api/todos", method = RequestMethod.GET)
	@GetMapping("/api/todos")
	public List<Todo> list(){
		return finder.getAll();
	}
	
	//@RequestMapping(path ="/api/todos", method = RequestMethod.POST)
	@PostMapping("/api/todos")
	public void create() {
		
	}
}
