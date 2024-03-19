package com.socialwebbspring.service;

import com.socialwebbspring.model.SubjectGPA;
import com.socialwebbspring.repository.SubjectGPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectGPAService {

    @Autowired
    private SubjectGPARepository subjectGPARepository;

    public void saveSubject(int userId, String name, int mark, double gpa) {
        SubjectGPA subject = new SubjectGPA();
        subject.setUserId(userId);
        subject.setName(name);
        subject.setMark(mark);
        subject.setGpa(gpa);
        subjectGPARepository.save(subject);
    }

    public List<SubjectGPA> getAllSubjects(int userId) {
        // Retrieve all subjects data for the user
        return subjectGPARepository.findByUserId(userId);
    }
    public void deleteSubject(int id) {
        subjectGPARepository.deleteById(id);
    }
}
