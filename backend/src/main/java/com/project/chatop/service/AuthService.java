package com.project.chatop.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.chatop.exception.ResourceNotFoundException;
import com.project.chatop.model.User;
import com.project.chatop.repository.UserRepository;
import com.project.chatop.security.JwtUtil;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	
	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}
	
	/*
	 Enregistre un nouvel utilisateur dans la base de données.
	 Vérifie que l'email n'est pas déjà utilisé et encode le mot de passe.
	*/
	public User register (String name, String email, String password) {
		if(userRepository.existsByEmail(email)) {
			throw new RuntimeException("Email is already in use");
		}
		
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		
		return userRepository.save(user);
	}
	
	/*
	 Authentifie un utilisateur avec email et mot de passe.
	 Si les informations sont correctes, génère un JWT.
	*/
	public String login(String email, String password) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		
		User user = optionalUser
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Incorrect password");
		}
		
		String token = jwtUtil.generateToken(user);	
		
		return token;
	}
}
