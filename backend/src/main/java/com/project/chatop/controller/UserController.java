package com.project.chatop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatop.model.User;
import com.project.chatop.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private final UserRepository userRepository;
	
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@GetMapping
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping("/{email}")
	public User getUserByEmail(@PathVariable String email) {
		return userRepository.findByEmail(email);
	}
}
