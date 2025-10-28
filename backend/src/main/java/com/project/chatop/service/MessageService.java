package com.project.chatop.service;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.chatop.dto.MessageDto;
import com.project.chatop.exception.ResourceNotFoundException;
import com.project.chatop.model.Message;
import com.project.chatop.model.Rental;
import com.project.chatop.repository.MessageRepository;
import com.project.chatop.repository.RentalRepository;

import io.jsonwebtoken.Claims;

@Service
public class MessageService {
	final MessageRepository messageRepository; 
	private final RentalRepository rentalRepository;
	
	public MessageService(MessageRepository messageRepository, RentalRepository rentalRepository) {
		this.messageRepository = messageRepository;
		this.rentalRepository = rentalRepository;
	}
	
	/**
	 Crée un message lié à une location.
	 Le message est associé à l'utilisateur courant grâce au JWT.
	*/
	public Message createMessage(MessageDto messageDto) {
		LocalDateTime date = LocalDateTime.now();
		
		Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer currentUserId = claims.get("userId", Integer.class);
        
        Rental rental = rentalRepository.findById(messageDto.getRentalId())
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));
		
		Message message = new Message();
		
		message.setUserId(currentUserId);
		message.setRentalId(rental.getId());
		message.setMessage(messageDto.getMessage());
		message.setCreatedAt(date);
	
		return messageRepository.save(message);
	}
}
