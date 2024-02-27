package com.socialwebbspring.model;

import javax.persistence.*;
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne // Many posts can be associated with one user
    @JoinColumn(name = "user_Id", referencedColumnName = "id")
    private User user;

    @Column(name = "post_image")
    private String postImage;

    @Column(name = "likes")
    private int likes;

    @Column(name = "caption", columnDefinition = "TEXT")
    private String caption;

    @Column(name = "tags")
    private String tags;

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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
}

