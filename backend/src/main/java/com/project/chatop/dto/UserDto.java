package com.project.chatop.dto;


public class UserDto {
	private Integer id;
	private String name;
    private String email;
    private String created_at;
    private String updated_at;

    public UserDto(Integer id, String name, String email, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
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
