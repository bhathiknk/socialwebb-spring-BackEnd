package com.socialwebbspring.dto;

public class GradeTrackingDto {

    private String subName;
    private String subjectMarks;
    private String gpa;

    // Constructors, getters, and setters

    public GradeTrackingDto() {
    }

    public GradeTrackingDto(String subName, String subjectMarks, String gpa) {
        this.subName = subName;
        this.subjectMarks = subjectMarks;
        this.gpa = gpa;
    }


    // Getters and setters
    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubjectMarks() {
        return subjectMarks;
    }

    public void setSubjectMarks(String subjectMarks) {
        this.subjectMarks = subjectMarks;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }
}
