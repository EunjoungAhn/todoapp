package todoapp.web;

import javax.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import todoapp.security.UserSession;
import todoapp.web.model.UserProfile;

@RestController
public class UserRestController {
	
	/* 세션 정보를 핸드러의 인수로 받기 때문에 필요 없다.
	private final UserSessionRepository userSessionRepository;
	
	public UserRestController(UserSessionRepository userSessionRepository) {
		this.userSessionRepository = userSessionRepository;
	}
	*/
	
	@GetMapping("/api/user/profile")
	@RolesAllowed("ROLE_USER")
	public UserProfile userProfile(UserSession userSession){
		return new UserProfile(userSession.getUser());
	}
}
 