package com.project.chatop.service;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.chatop.dto.MessageDto;
import com.project.chatop.model.Message;
import com.project.chatop.repository.MessageRepository;

import io.jsonwebtoken.Claims;

@Service
public class MessageService {
	final MessageRepository messageRepository; 
	
	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}
	
	public Message createMessage(MessageDto messageDto) {
		LocalDateTime date = LocalDateTime.now();
		
		Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer ownerId = claims.get("userId", Integer.class);
		
		Message message = new Message();
		
		message.setUserId(ownerId);
		message.setRentalId(messageDto.getRentalId());
		message.setMessage(messageDto.getMessage());
		message.setCreatedAt(date);
	
		return messageRepository.save(message);
	}
}
