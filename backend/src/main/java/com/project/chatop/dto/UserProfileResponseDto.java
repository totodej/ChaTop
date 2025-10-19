package com.project.chatop.dto;

import java.time.LocalDateTime;

public class UserProfileResponseDto {
	private Integer id;
	private String name;
    private String email;
    private String created_at;
    private String updated_at;

    public UserProfileResponseDto(Integer id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.created_at = createdAt != null ? createdAt.toString() : null;
        this.updated_at = updatedAt != null ? updatedAt.toString() : null;
    }

    public Integer getId() {
        return id;
    }
    
    public String getName() {
    	return name;
    }

    public String getEmail() {
        return email;
    }
    
    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
