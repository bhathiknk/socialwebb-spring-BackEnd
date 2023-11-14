package com.socialwebbspring.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.socialwebbspring.dto.GradeTrackingDto; // Corrected import
import com.socialwebbspring.model.GradeTracking;
import com.socialwebbspring.repository.GradeTrackingRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grades")//  /api/grades
public class GradeTrackingController {

    @Autowired
    private GradeTrackingRepository gradeTrackingRepository;

    @PostMapping("/save")// /save
    public ResponseEntity<String> saveGrades(@RequestBody List<GradeTrackingDto> gradeDTOs) { // Corrected parameter type
        // Convert DTOs to entities and save to the database
        List<GradeTracking> grades = convertToEntities(gradeDTOs);
        gradeTrackingRepository.saveAll(grades);

        return ResponseEntity.ok("Grades saved successfully");
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GradeTrackingDto>> getAllGrades() { // Corrected return type
        // Retrieve all grades from the database
        List<GradeTracking> grades = gradeTrackingRepository.findAll();

        // Convert entities to DTOs and send to the frontend
        List<GradeTrackingDto> gradeDTOs = convertToDTOs(grades);

        return ResponseEntity.ok(gradeDTOs);
    }

    // Additional methods as needed

    // Helper method to convert entities to DTOs
    private List<GradeTrackingDto> convertToDTOs(List<GradeTracking> grades) {
        return grades.stream()
                .map(grade -> new GradeTrackingDto(grade.getSubName(), grade.getSubjectMarks(), grade.getGpa()))
                .collect(Collectors.toList());
    }

    // Helper method to convert DTOs to entities
    private List<GradeTracking> convertToEntities(List<GradeTrackingDto> gradeDTOs) {
        return gradeDTOs.stream()
                .map(gradeDTO -> new GradeTracking(gradeDTO.getSubName(), gradeDTO.getSubjectMarks(), gradeDTO.getGpa()))
                .collect(Collectors.toList());
    }
}
