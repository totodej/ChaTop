package com.project.chatop.service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.chatop.dto.UserDto;
import com.project.chatop.exception.ResourceNotFoundException;
import com.project.chatop.model.User;
import com.project.chatop.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/*
	 Récupère un utilisateur par son ID et le transforme en DTO.
	*/
	public UserDto getUserById(Integer id) {
        
		User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String createdAtStr = user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null;
        String updatedAtStr = user.getUpdatedAt() != null ? user.getUpdatedAt().format(formatter) : null;
        
        UserDto userDto = new UserDto(
        		user.getId(),
        		user.getName(), 
        		user.getEmail(), 
        		createdAtStr, 
        		updatedAtStr);
        
        return userDto;
	}
}
