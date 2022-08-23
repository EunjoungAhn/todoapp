package todoapp;

import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.BodyInserters.FormInserter;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import todoapp.security.UserSessionRepository;
import todoapp.web.LoginController;
import todoapp.web.model.SiteProperties;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //랜덤 포트를 쓰겠다.
public class LoginControllerTests {
	
	@Autowired ApplicationContext applicationContext;
	@Autowired UserSessionRepository userSessionRepository;
	@Autowired LoginController controller;
	
	WebClient webClient;
	WebTestClient webTestClient;
	
	//스프링 부트가 내장된 서버로 application 서버를 실행하고 이때 랜덤하게 port를 쓴다.
	//랜덤 port를 @LocalServerPort 로 받아와서 사용한다. 
	@BeforeEach
	void setUp(@LocalServerPort int port) {//스프링이 테스트 메서드의 파라미터를 받을 수 있게 제공해 준다.
		webClient = WebClient.create("http://localhost:" + port);
		//webTestClient 만드는 3가지 방법이 있으며, 1. applicationContext 2. 컨트롤러(핸들러) 3. 서버
		//3. 서버를 대상으로 만들어 본다.
		webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:"+port).build();
	}
	
	@Test
	void 스프링_컨테이너는_자동클래스탐지로_로그인컨트롤러를_찾아_등록해요() {
		Assertions.assertTrue(applicationContext.containsBean("loginController"));
	}

	
	@Test //특별히 Mock 테스트의 경우 @Autowired를 붙여서 인자를 받아와야 한다.
	void 인증되지않은_사용자가_로그인화면에_접근하면_로그인화면을_보여준다(@Autowired SiteProperties siteProperties) throws Exception {
		webTestClient.get().uri("/login").exchange().expectAll(
			spec -> spec.expectStatus().isOk(),
			spec -> spec.expectBody()//사용을 위해 login.html 파일을 login.xhtml 로 변경해야한다.
				.xpath("//input[@name='username']").exists()
				.xpath("//input[@name='password']").exists()
				.xpath("//a[text()='"+siteProperties.getAuthor()+"']").exists()
		);
	}
	
	@Test
	void 인증된_사용자가_로그인화면에_접근하면_할일관리화면으로_전환시킨다() throws Exception {
		//기본적으로 http는 무상태 이다. 쿠키로 매니징을 해줘야 하지만 안하고 있어서 
		//쿠키 제공을 위해 작성한다.
		MultiValueMap<String, String> cookieStore = new LinkedMultiValueMap<>();
		
		//1. 사용자가 로그인을 한다.
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
			formData.add("username", "user");
			formData.add("password", "password");
		//로그인 요청을 보내기 위해 바디를 준비
		FormInserter<String> formBody = BodyInserters.fromFormData(formData);
		webClient.post().uri("/login").body(formBody).exchangeToMono(response ->{
			//쿠키를 반복하면서 쿠키를 복제
			response.cookies().forEach((name, cookies) -> {
				cookies.forEach(cookie -> cookieStore.add(name, cookie.getValue()));
			});
			return Mono.empty();
		}).block();
			
		//2. 로그인된 사용자가 로그인 화면 접근시 상황을 검증한다.
		webTestClient.get().uri("/login").cookies(cookies -> cookies.addAll(cookieStore)).exchange().expectAll(
				sepc -> sepc.expectStatus().is3xxRedirection(),
				sepc -> sepc.expectHeader().value(HttpHeaders.LOCATION, StringEndsWith.endsWith("/todos"))
				
		);
	}
	
}
