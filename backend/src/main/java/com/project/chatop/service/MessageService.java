package com.project.chatop.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.project.chatop.model.Message;
import com.project.chatop.repository.MessageRepository;

@Service
public class MessageService {
	final MessageRepository messageRepository; 
	
	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}
	
	public Message createMessage(Message message) {
		LocalDateTime date = LocalDateTime.now();
		message.setMessage(message.getMessage());
		message.setCreatedAt(date);
	
		return messageRepository.save(message);
	}
	
	
}
