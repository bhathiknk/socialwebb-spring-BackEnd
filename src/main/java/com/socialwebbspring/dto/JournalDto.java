package com.socialwebbspring.dto;

// JournalDto.java
public class JournalDto {
    private String date;
    private Integer  userId;
    private String mood;
    private String goals;
    private String textFileName; // Added field for the PDF file name

    public static String content;

    // Getters and setters
    // ...

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

    public static String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
