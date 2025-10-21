package com.project.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RentalDto {
    private Integer id;
    
    private String name;
    
    private Integer surface;
    
    private float price;
    
    private String picture;
    
    private String description;
    
    private Integer ownerId;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("updated_at")
    private String updatedAt;

    public RentalDto(Integer id, String name, Integer surface, float price, String picture, String description, Integer ownerId, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getSurface() {
		return surface;
	}

	public float getPrice() {
		return price;
	}

	public String getPicture() {
		return picture;
	}

	public String getDescription() {
		return description;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

}
