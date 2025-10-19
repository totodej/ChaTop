package com.project.chatop.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatop.dto.LoginRequest;
import com.project.chatop.dto.LoginResponseDto;
import com.project.chatop.dto.RegisterRequest;
import com.project.chatop.dto.UserProfileResponseDto;
import com.project.chatop.model.User;
import com.project.chatop.service.AuthService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {	
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/register")
	/*
	public User register(@RequestBody RegisterRequest request) {
		return authService.register(request.getName(), request.getEmail(), request.getPassword());
	}
	*/
	public ResponseEntity<LoginResponseDto> register(@RequestBody RegisterRequest request) {
		User user = authService.register(request.getName(), request.getEmail(), request.getPassword());
		String token = authService.login(request.getEmail(), request.getPassword());
		return ResponseEntity.ok(new LoginResponseDto(token));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			String token = authService.login(request.getEmail(), request.getPassword());
			return ResponseEntity.ok(new LoginResponseDto(token));
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "error"));
		}
		
	}
	
	@GetMapping("/me")
	public ResponseEntity<Object> me() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
	        return ResponseEntity.ok(new Object());
	    }
	    
    	var claims = (io.jsonwebtoken.Claims) authentication.getPrincipal();
	    
	    UserProfileResponseDto userProfile = new UserProfileResponseDto(
	    		(Integer) claims.get("userId"),
	    		(String) claims.get("name"),
	    		(String) claims.get("email"),
	            claims.get("createdAt") != null ? LocalDateTime.parse((String) claims.get("createdAt")) : null,
	            claims.get("updatedAt") != null ? LocalDateTime.parse((String) claims.get("updatedAt")) : null
	    );
	    

	    return ResponseEntity.ok(userProfile);
    	    
	}
}
