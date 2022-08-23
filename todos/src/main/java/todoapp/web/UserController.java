package todoapp.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import todoapp.core.user.application.ProfilePictureChanger;
import todoapp.core.user.domain.ProfilePicture;
import todoapp.core.user.domain.ProfilePictureStorage;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;

@Controller
public class UserController {
	
	/*
	private final ProfilePictureStorage profilePictureStorage;
	
	public UserController(ProfilePictureStorage profilePictureStorage) {
		this.profilePictureStorage = profilePictureStorage;
	}
	*/
	
	//로그인된 사용자의 정보의 ProfilePicture를 그 즉시 반환하였다.
	@RequestMapping("/user/profile-picture")
	@RolesAllowed("ROLE_USER")
	//public @ResponseBody Resource profilePicture(UserSession userSession) throws IOException {
	public ProfilePicture profilePicture(UserSession userSession) throws IOException {
		//프로필 이미지의 Uri를 이용해서 경로 정보까지 얻어 왔다.
		//Path profilePicturePath = Paths.get(userSession.getUser().getProfilePicture().getUri());
		//return Files.readAllBytes(profilePicturePath);
		//return profilePictureStorage.load(userSession.getUser().getProfilePicture().getUri());
		return userSession.getUser().getProfilePicture();
	}
	
	public static class ProfilePictureReturnValueHandler implements HandlerMethodReturnValueHandler{

		private final ProfilePictureStorage profilePictureStorage;
		
		public ProfilePictureReturnValueHandler(ProfilePictureStorage profilePictureStorage) {
			this.profilePictureStorage = profilePictureStorage;
		}
		
		@Override
		public boolean supportsReturnType(MethodParameter returnType) {
			return ProfilePicture.class.isAssignableFrom(returnType.getParameterType());
		}

		@Override
		public void handleReturnValue(Object returnValue, MethodParameter returnType,
				ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
			HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
			//returnVale 는 핸들러가 반환한 값
			Resource ProfilePicture = profilePictureStorage.load(((ProfilePicture) returnValue).getUri());
			ProfilePicture.getInputStream().transferTo(response.getOutputStream());
		}
		
	}
}
