package com.socialwebbspring.dto;

public class ModuleQuestionDTO {
    private Integer moduleId;
    private Integer userId;
    private String moduleQuestion;
    private String moduleAnswer;

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getModuleQuestion() {
        return moduleQuestion;
    }

    public void setModuleQuestion(String moduleQuestion) {
        this.moduleQuestion = moduleQuestion;
    }

    public String getModuleAnswer() {
        return moduleAnswer;
    }

    public void setModuleAnswer(String moduleAnswer) {
        this.moduleAnswer = moduleAnswer;
    }
}
