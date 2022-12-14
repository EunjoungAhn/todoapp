package todoapp.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import todoapp.security.AccessDeniedException;
import todoapp.security.UnauthorizedAccessException;
import todoapp.web.model.SiteProperties;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	private final SiteProperties siteProperties;
	
	public GlobalControllerAdvice(SiteProperties siteProperties) {
		this.siteProperties = siteProperties;
	}
	
	@ModelAttribute("site")
	public SiteProperties siteProperties() {
		return siteProperties;
	}
	
	/*
	@ExceptionHandler(UnauthorizedAccessException.class)
	public ResponseEntity<Map<String, Object>> handleUnauthorizedAccessException(UnauthorizedAccessException error){
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("error", error.getClass().getSimpleName());
		attributes.put("message", error.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(attributes);//권한이 없을때는 401
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, Object>> handleUnauthorizedAccessException(AccessDeniedException error){
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("error", error.getClass().getSimpleName());
		attributes.put("message", error.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(attributes);//403
	}
	*/
}
