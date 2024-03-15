package com.socialwebbspring.model;

import javax.persistence.*;

@Entity
@Table(name = "module_question")
public class ModuleQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "module_question")
    private String moduleQuestion;
    @Column(name = "module_answer")
    private String moduleAnswer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
