package com.socialwebbspring.controller;

import com.socialwebbspring.dto.ModuleQuestionDTO;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.ModuleQuestion;
import com.socialwebbspring.service.ModuleQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ModuleQuestionController {

    @Autowired
    private ModuleQuestionService moduleQuestionService;

    @PostMapping("/module-question")
    public ResponseEntity<String> saveModuleQuestion(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody ModuleQuestionDTO moduleQuestionDTO) {
        try {
            moduleQuestionService.saveModuleQuestion(token, moduleQuestionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Module question saved successfully");
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/module-questions/{moduleId}")
    public ResponseEntity<List<ModuleQuestion>> getModuleQuestionsByModuleId(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Integer moduleId) {
        try {
            List<ModuleQuestion> moduleQuestions = moduleQuestionService.getModuleQuestionsByUserIdAndModuleId(token, moduleId);
            return ResponseEntity.ok(moduleQuestions);
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
