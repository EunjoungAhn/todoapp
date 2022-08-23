package todoapp.web;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import todoapp.core.user.application.ProfilePictureChanger;
import todoapp.core.user.domain.ProfilePicture;
import todoapp.core.user.domain.User;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;
import todoapp.web.model.UserProfile;

@RestController
@RolesAllowed("ROLE_USER")
public class UserRestController {
	
	/* 세션 정보를 핸드러의 인수로 받기 때문에 필요 없다.
	private final UserSessionRepository userSessionRepository;
	*/
	
	private final ProfilePictureChanger profilePictureChanger;
	private final UserSessionRepository userSessionRepository;
	
	public UserRestController(ProfilePictureChanger profilePictureChanger, UserSessionRepository userSessionRepository) {
		this.profilePictureChanger = profilePictureChanger;
		this.userSessionRepository = userSessionRepository;
	}
	
	@GetMapping("/api/user/profile")
	public UserProfile userProfile(UserSession userSession){
		return new UserProfile(userSession.getUser());
	}
	
	@PostMapping("/api/user/profile-picture")
	public UserProfile changeProfilePicture(MultipartFile profilePicture, UserSession userSession) throws IOException {
		//업로드된 유저의 이미지를 받아서 변경처리하기  
		
		//업로드된 프로필 이미지 파일 저장
		Path basePath = Paths.get("./files/user-profile-picture");
		if(!basePath.toFile().exists()) {
			basePath.toFile().mkdirs();
		}
		Path profilepicturePath = basePath.resolve(profilePicture.getOriginalFilename());
		profilePicture.transferTo(profilepicturePath);
		
		// 프로필 이미지 변경 후 세션을 갱신하기
		User updatedUser = profilePictureChanger.change(userSession.getName(), new ProfilePicture(profilepicturePath.toUri()));
		//업데이트 된 유저 정보 때문에 세션을 업데이트해주어야 한다.
		//왜냐면 현재 세션 사용자 정보는 과거의 정보이기 때문이다.
		userSessionRepository.set(new UserSession(updatedUser));//업데이트된 사용자 정보로 갱신
		
		return new UserProfile(updatedUser); //업데이트된 사용자 정보로 갱신
	}
}
 