package com.project.chatop.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatop.dto.MessageDto;
import com.project.chatop.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
	
	final MessageService messageService;
	
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}
	
	@PostMapping
	public ResponseEntity<Map<String, String>> createMessage(@RequestBody MessageDto messageDto) {
		System.out.println("Message reçu : " + messageDto.getUserId() + ", " + messageDto.getRentalId());
		messageService.createMessage(messageDto);
		return ResponseEntity.ok(Map.of("message", "Message send with success"));
	}
}
