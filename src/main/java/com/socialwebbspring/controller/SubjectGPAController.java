package com.socialwebbspring.controller;

import com.socialwebbspring.dto.SubjectGPADTO;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.SubjectGPA;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.AuthenticationService;
import com.socialwebbspring.service.SubjectGPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
public class SubjectGPAController {

    @Autowired
    private SubjectGPAService subjectGPAService;

    @Autowired
    private AuthenticationService authenticationService; // Inject AuthenticationService

    @PostMapping("/save")
    public ResponseEntity<String> saveSubjectData(@RequestBody SubjectGPADTO subjectDataRequest, @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from the token
            int userId = extractUserIdFromToken(token);

            // Calculate GPA
            double gpa = calculateGPA(subjectDataRequest.getMark());

            // Save subject data
            subjectGPAService.saveSubject(userId, subjectDataRequest.getName(), subjectDataRequest.getMark(), gpa);

            return ResponseEntity.ok("Subject data saved successfully");
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not valid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save subject data");
        }
    }

    private int extractUserIdFromToken(String token) throws AuthenticationFailException {
        // Extract user ID from token using AuthenticationService
        User user = authenticationService.getUser(token);
        if (user == null) {
            throw new AuthenticationFailException("Token not valid");
        }
        return user.getId();
    }

    private double calculateGPA(int mark) {
        if (mark >= 90) return 4.0;
        else if (mark >= 85) return 3.7;
        else if (mark >= 80) return 3.3;
        else if (mark >= 75) return 3.0;
        else if (mark >= 65) return 2.7;
        else if (mark >= 55) return 2.3;
        else if (mark >= 45) return 2.0;
        else return 0.0;
    }
    @GetMapping("/all")
    public ResponseEntity<List<SubjectGPA>> getAllSubjects(@RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from the token
            int userId = extractUserIdFromToken(token);

            // Retrieve subjects data for the user
            List<SubjectGPA> subjects = subjectGPAService.getAllSubjects(userId);

            return ResponseEntity.ok(subjects);
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable("id") int id, @RequestHeader("Authorization") String token) {
        try {
            // Check authentication and authorization here if needed
            subjectGPAService.deleteSubject(id);
            return ResponseEntity.ok("Subject deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete subject");
        }
    }

    @GetMapping("/allWithFinalGPA")
    public ResponseEntity<Map<String, Object>> getAllSubjectsWithFinalGPA(@RequestHeader("Authorization") String token) {
        try {
            int userId = extractUserIdFromToken(token);
            List<SubjectGPA> subjects = subjectGPAService.getAllSubjects(userId);
            double finalGPA = subjectGPAService.calculateFinalGPA(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("subjects", subjects);
            response.put("finalGPA", finalGPA);
            return ResponseEntity.ok(response);
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
