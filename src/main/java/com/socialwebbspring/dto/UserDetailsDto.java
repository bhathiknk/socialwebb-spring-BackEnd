package com.socialwebbspring.dto;
public class UserDetailsDto {
    private Integer Id;
    private String profileImage;
    private String userName;
    private String email;
    private String text;


    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDetailsDto(Integer id, String profileImage, String userName, String email, String text) {
        Id = id;
        this.profileImage = profileImage;
        this.userName = userName;
        this.email = email;
        this.text = text;
    }
}
