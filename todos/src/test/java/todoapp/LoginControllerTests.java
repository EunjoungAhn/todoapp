package todoapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import todoapp.core.user.domain.User;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;
import todoapp.web.LoginController;
import todoapp.web.model.SiteProperties;

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
	
	MockMvc mockMvc;
	
	@BeforeEach
	void setUp(WebApplicationContext wac) {//스프링이 테스트 메서드의 파라미터를 받을 수 있게 제공해 준다.
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	void 스프링_컨테이너는_자동클래스탐지로_로그인컨트롤러를_찾아_등록해요() {
		Assertions.assertTrue(applicationContext.containsBean("loginController"));
	}

	
	@Test //특별히 Mock 테스트의 경우 @Autowired를 붙여서 인자를 받아와야 한다.
	void 인증되지않은_사용자가_로그인화면에_접근하면_로그인화면을_보여준다(@Autowired SiteProperties siteProperties) throws Exception {
		/*스프링의 테스트 컨텍스트가 해주기 때문제 제거 가능하다.
		MockHttpServletRequest requset = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(requset, response));
		
		LoginController controller = applicationContext.getBean(LoginController.class);
		 */
		//Assertions.assertEquals("login", controller.loginForm()); //컨트롤러가 아닌 MockMVC로 동작 하도록 수정
		//RequestContextHolder.resetRequestAttributes();
		
		RequestBuilder request = MockMvcRequestBuilders.get("/login");
		
		//응답 값이 200 인지? 뷰 이름이 login인지? site 라는 모델이 있는지? 그 값이 site값이 맞는지?
		mockMvc.perform(request).andExpectAll(
				MockMvcResultMatchers.status().isOk(),
				MockMvcResultMatchers.view().name("login"),
				MockMvcResultMatchers.model().attribute("site", siteProperties)
		);
	}
	
	@Test
	void 인증된_사용자가_로그인화면에_접근하면_할일관리화면으로_전환시킨다() throws Exception {
		/*스프링의 테스트 컨텍스트가 해주기 때문제 제거 가능하다.
		MockHttpServletRequest requset = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(requset, response));
		
		//로그인 되었을때 처리
		//UserSessionRepository userRepository = applicationContext.getBean(UserSessionRepository.class);
		userSessionRepository.set(new UserSession(new User("tester", "")));
		 */
		
		//LoginController controller = applicationContext.getBean(LoginController.class);
		//Assertions.assertEquals("redirect:/todos", controller.loginForm()); //전체 테스트를 위해 수정
		
		//RequestContextHolder.resetRequestAttributes();
		
		//get 요청으로 로그인 요청이간후
		RequestBuilder request = MockMvcRequestBuilders.get("/login").with(mockRequest -> {
			//로그인이 되어 있는 상태를 처리하는 코드
			RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));
			userSessionRepository.set(new UserSession(new User("tester", "")));
			return mockRequest;
		});
		
		mockMvc.perform(request).andExpectAll(
				MockMvcResultMatchers.status().is3xxRedirection(),
				MockMvcResultMatchers.view().name("redirect:/todos")
		);
	}
	
}
