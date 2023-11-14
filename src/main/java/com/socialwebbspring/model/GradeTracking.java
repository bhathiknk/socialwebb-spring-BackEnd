package com.socialwebbspring.model;



import javax.persistence.*;


@Entity
@Table(name = "grade_tracking")
public class GradeTracking {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sub_name")
    private String subName;

    @Column(name = "subject_marks ")
    private String subjectMarks;

    @Column(name = "gpa ")
    private String gpa;



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



    public GradeTracking(String subName, String subjectMarks, String gpa) {
        this.subName = subName;
        this.subjectMarks = subjectMarks;
        this.gpa = gpa;
    }

    public GradeTracking() {
    }
}
