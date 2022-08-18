package todoapp.data;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import todoapp.core.todos.domain.Todo;
import todoapp.core.todos.domain.TodoRepository;

@Component
@ConditionalOnProperty(name="todos.data.initialize", havingValue = "true")
public class TodosDataInitializer implements InitializingBean, ApplicationRunner, CommandLineRunner{

	private final TodoRepository todoRepository;
	
	public TodosDataInitializer(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}
	
	//스프링 프레임워크가 제공하는 데이터를 초기화 시켜주는 작업
	@Override
	public void afterPropertiesSet() throws Exception {
		todoRepository.save(Todo.create("Task one1"));
	}

	//스프링 부트가 제공하는 데이터 초기화 방법
	@Override
	public void run(ApplicationArguments args) throws Exception {
		todoRepository.save(Todo.create("Task two2"));
	}

	@Override
	public void run(String... args) throws Exception {
		todoRepository.save(Todo.create("Task three3"));
	}
	
	
	
}
