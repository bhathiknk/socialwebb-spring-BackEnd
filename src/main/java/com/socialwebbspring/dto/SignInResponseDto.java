package com.socialwebbspring.dto;

public class SignInResponseDto {

    private String status;
    private String token;
    private Integer userId; // Add userId property


    public SignInResponseDto(String status, String token) {
        this.status = status;
        this.token = token;
        this.userId=userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}