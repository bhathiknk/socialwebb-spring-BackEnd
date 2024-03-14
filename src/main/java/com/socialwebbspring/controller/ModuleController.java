package com.socialwebbspring.controller;

import com.socialwebbspring.dto.ModuleDTO;
import com.socialwebbspring.dto.QuestionDTO;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.ModuleEntity;
import com.socialwebbspring.service.ModuleService;
import com.socialwebbspring.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @PostMapping("/create-module")
    public ResponseEntity<String> saveModule(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody ModuleDTO moduleDTO) {
        try {
            moduleService.saveModule(token, moduleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Module saved successfully");
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/modules")
    public ResponseEntity<List<ModuleEntity>> getAllModules(
            @RequestHeader(name = "Authorization") String token) {
        try {
            List<ModuleEntity> modules = moduleService.getAllModules(token);
            return ResponseEntity.ok().body(modules);
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}