package com.socialwebbspring.dto;

public class UserDetailsDto {

    private String userName;
    private String email;



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDetailsDto(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }
}
