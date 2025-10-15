package com.project.chatop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatop.model.Message;
import com.project.chatop.model.User;
import com.project.chatop.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
	
	final MessageService messageService;
	
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}
	
	@PostMapping
	public ResponseEntity<Message> createMessage(@RequestBody Message message) {
		System.out.println("Message reçu : " + message.getUserId() + ", " + message.getRentalId());
		Message newMessage = messageService.createMessage(message);
		return ResponseEntity.ok(newMessage);
	}

}
