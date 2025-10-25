package com.project.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDto {
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("rental_id")
    private Integer rentalId;

    private String message;

    public MessageDto() {}

    public MessageDto(Integer userId, Integer rentalId, String message) {
        this.userId = userId;
        this.rentalId = rentalId;
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
