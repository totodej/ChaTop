package com.project.chatop.controller;

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

import com.project.chatop.dto.LoginRequestDto;
import com.project.chatop.dto.LoginResponseDto;
import com.project.chatop.dto.RegisterRequestDto;
import com.project.chatop.dto.UserDto;
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
	public ResponseEntity<LoginResponseDto> register(@RequestBody RegisterRequestDto request) {
		authService.register(request.getName(), request.getEmail(), request.getPassword());
		String token = authService.login(request.getEmail(), request.getPassword());
		return ResponseEntity.ok(new LoginResponseDto(token));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
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

	    if (authentication == null || "anonymousUser".equals(authentication.getPrincipal())) {
	        return ResponseEntity.ok(new Object());
	    }

	    Object principal = authentication.getPrincipal();
	    if (!(principal instanceof io.jsonwebtoken.Claims claims)) {
	        return ResponseEntity.status(401).body(Map.of("message", "Invalid token"));
	    }

	    UserDto user = new UserDto(
	        (Integer) claims.get("userId"),
	        (String) claims.get("name"),
	        (String) claims.get("email"),
	        (String) claims.get("createdAt"),
	        (String) claims.get("updatedAt")
	    );

	    return ResponseEntity.ok(user);
	}
}
