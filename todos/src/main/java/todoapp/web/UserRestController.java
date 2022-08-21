package todoapp.web;

import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import todoapp.core.user.domain.User;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;
import todoapp.web.model.UserProfile;

@RestController
public class UserRestController {
	
	private final UserSessionRepository userSessionRepository;
	
	public UserRestController(UserSessionRepository userSessionRepository) {
		this.userSessionRepository = userSessionRepository;
	}
	
	@GetMapping("/api/user/profile")
	//public ResponseEntity<UserProfile> userProfile(@SessionAttribute("user") User user){
	public ResponseEntity<UserProfile> userProfile(){
		UserSession userSession = userSessionRepository.get();
		//사용자 정보를 세션 정보로 부터 가져오기
		//User user = (User) session.getAttribute("user");
		//if(Objects.nonNull(user)) {
		if(Objects.nonNull(userSession)) {
			//로그인 사용자의 정보가 세션에 들어 있다면 반환
			//return ResponseEntity.ok(new UserProfile(user)); //200
			return ResponseEntity.ok(new UserProfile(userSession.getUser())); //200
		}
		//user 객체가 null 이라면
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401
	}
}
 