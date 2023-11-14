package com.socialwebbspring.service;

import com.socialwebbspring.dto.GradeTrackingDto;
import com.socialwebbspring.model.GradeTracking;
import com.socialwebbspring.repository.GradeTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeTrackingService {

    private final GradeTrackingRepository gradeTrackingRepository;

    @Autowired
    public GradeTrackingService(GradeTrackingRepository gradeTrackingRepository) {
        this.gradeTrackingRepository = gradeTrackingRepository;
    }

    public void saveGrades(List<GradeTrackingDto> saveSubjects) {
        List<GradeTracking> grades = convertToEntities(saveSubjects);
        gradeTrackingRepository.saveAll(grades);
    }

    public List<GradeTrackingDto> getAllGrades() {
        List<GradeTracking> grades = gradeTrackingRepository.findAll();
        return convertToDTOs(grades);
    }

    private List<GradeTrackingDto> convertToDTOs(List<GradeTracking> grades) {
        return grades.stream()
                .map(grade -> new GradeTrackingDto(grade.getSubName(), grade.getSubjectMarks(), grade.getGpa()))
                .collect(Collectors.toList());
    }

    private List<GradeTracking> convertToEntities(List<GradeTrackingDto> gradeDTOs) {
        return gradeDTOs.stream()
                .map(gradeDTO -> new GradeTracking(gradeDTO.getSubName(), gradeDTO.getSubjectMarks(), gradeDTO.getGpa()))
                .collect(Collectors.toList());
    }
}
