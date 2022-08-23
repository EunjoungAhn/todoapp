package todoapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import todoapp.core.user.domain.User;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;
import todoapp.web.LoginController;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = TodosApplication.class)
//@WebAppConfiguration
//@SpringJUnitConfig(TodosApplication.class)// 위의 ExtendWith, ContextConfiguration 역할을 다 감당해 주는 애노테이션이다.
@SpringJUnitWebConfig(TodosApplication.class)//ExtendWith, ContextConfiguration, WebAppConfiguration 3가지의 기능을 다 담당하는 애노테이션이다.
public class LoginControllerTests {
	
	/*
	private AnnotationConfigServletWebServerApplicationContext applicationContext;
	
	//Test가 실행되기 전
	@BeforeEach
	void setUp() throws Exception{
		//스프링 컨테이너 생성
		applicationContext = new AnnotationConfigServletWebServerApplicationContext(TodosApplication.class);
	}
	
	//테스트가 실행된 후
	@AfterEach
	void tearDown() throws Exception{
		//스프링 컨테이너 종료
		if(Objects.nonNull(applicationContext)) {
			applicationContext.close();
			
		}
	}
	*/
	
	@Autowired ApplicationContext applicationContext;
	@Autowired UserSessionRepository userSessionRepository;
	@Autowired LoginController controller;
	
	@Test
	void 스프링_컨테이너는_자동클래스탐지로_로그인컨트롤러를_찾아_등록해요() {
		Assertions.assertTrue(applicationContext.containsBean("loginController"));
	}

	@Test
	void 인증되지않은_사용자가_로그인화면에_접근하면_로그인화면을_보여준다() {
		/*스프링의 테스트 컨텍스트가 해주기 때문제 제거 가능하다.
		MockHttpServletRequest requset = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(requset, response));
		
		LoginController controller = applicationContext.getBean(LoginController.class);
		 */
		Assertions.assertEquals("login", controller.loginForm()); ;
		//RequestContextHolder.resetRequestAttributes();
	}
	
	@Test
	void 인증된_사용자가_로그인화면에_접근하면_할일관리화면으로_전환시킨다() {
		/*스프링의 테스트 컨텍스트가 해주기 때문제 제거 가능하다.
		MockHttpServletRequest requset = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(requset, response));
		
		//로그인 되었을때 처리
		//UserSessionRepository userRepository = applicationContext.getBean(UserSessionRepository.class);
		 */
		userSessionRepository.set(new UserSession(new User("tester", "")));
		
		//LoginController controller = applicationContext.getBean(LoginController.class);
		Assertions.assertEquals("redirect:/todos", controller.loginForm()); ;
		
		//RequestContextHolder.resetRequestAttributes();
	}
	
}
