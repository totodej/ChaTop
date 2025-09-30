package com.project.chatop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.oauth2.sdk.Request;
import com.project.chatop.dto.LoginRequest;
import com.project.chatop.dto.RegisterRequest;
import com.project.chatop.model.User;
import com.project.chatop.repository.UserRepository;
import com.project.chatop.service.AuthService;

@RestController
@RequestMapping("/auth")
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
	
	
}
