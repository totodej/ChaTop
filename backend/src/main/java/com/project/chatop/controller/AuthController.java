package com.project.chatop.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatop.dto.LoginRequest;
import com.project.chatop.dto.RegisterRequest;
import com.project.chatop.model.User;
import com.project.chatop.service.AuthService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {	
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/register")
	public User register(@RequestBody RegisterRequest request) {
		return authService.register(request.getName(), request.getEmail(), request.getPassword());
	}
	
	@PostMapping("/login")
	public User login(@RequestBody LoginRequest request) {
		return authService.login(request.getEmail(), request.getPassword());
	}
	
	@GetMapping("/me")
	public Object me(HttpSession session) {
		return session.getAttribute("user");
	}
}
