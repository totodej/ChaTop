package com.project.chatop.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	
	public User register (String name, String email, String password) {
		if(userRepository.existsByEmail(email)) {
			throw new RuntimeException("Email déjà utilisé");
		}
		
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		
		return userRepository.save(user);
	}
	
	public String login(String email, String password) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
		
		if(optionalUser.isEmpty()) {
			throw new RuntimeException("Utilisateur non trouvé");
		}
		
		User user = optionalUser.get();
		
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Mot de passe incorect");
		}
		
		String token = jwtUtil.generateToken(user);	
		
		return token;
	}
	
}
