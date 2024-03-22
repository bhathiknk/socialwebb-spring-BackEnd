package com.socialwebbspring.dto;

// Inside PostDto.java
public class PostDto {
    private Integer id;
    private Integer userId;
    private String username;
    private String profileImage;
    private String caption;
    private String tags;
    private String postImage;

    // Constructors
    public PostDto() {
    }

    public PostDto(Integer userId, String username, String profileImage, String caption, String tags, String postImage) {
        this.userId = userId;
        this.username = username;
        this.profileImage = profileImage;
        this.caption = caption;
        this.tags = tags;
        this.postImage = postImage;
    }

    // Getters and setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }
}
