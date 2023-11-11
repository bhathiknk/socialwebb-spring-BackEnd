package com.socialwebbspring.dto;

public class UserDetailsDto {
    private Integer Id;
    private String userName;
    private String email;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

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

    public UserDetailsDto(String userName, String email,Integer Id) {
        this.Id=Id;
        this.userName = userName;
        this.email = email;
    }
}
