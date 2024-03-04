package com.socialwebbspring.model;

import javax.persistence.*;

import static com.socialwebbspring.dto.JournalDto.content;

// Journal.java
@Entity
@Table(name = "journals")
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "date", nullable = false)
    private String date;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "mood")
    private String mood;

    @Column(name = "goals")
    private String goals;

    @Column(name = "text_file_name")
    private String textFileName;



    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String gettextFileName() {
        return textFileName;
    }

    public void settextFileName(String textFileName) {
        this.textFileName = textFileName;
    }

    public String getContent() {
        return content;

    }
}
