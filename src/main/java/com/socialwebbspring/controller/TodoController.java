package com.socialwebbspring.controller;

import com.socialwebbspring.dto.TodoDTO;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.Todo;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.AuthenticationService;
import com.socialwebbspring.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TodoController.java
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/save")
    public ResponseEntity<String> saveTodo(@RequestBody TodoDTO todoDTO, @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from the token
            int userId = extractUserIdFromToken(token);

            // Save todo data
            todoService.saveTodo(userId, todoDTO.getDate(), todoDTO.getTime(), todoDTO.getTodo());

            return ResponseEntity.ok("Todo saved successfully");
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not valid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save todo");
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

    @GetMapping("/fetch")
    public ResponseEntity<List<Todo>> getAllTodos(@RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from the token
            int userId = extractUserIdFromToken(token);

            // Fetch all todos for the user
            List<Todo> todos = todoService.getAllTodos(userId);
            return ResponseEntity.ok(todos);
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/{todoId}")
    public ResponseEntity<String> deleteTodo(@PathVariable int todoId, @RequestHeader("Authorization") String token) {
        try {
            // Extract user ID from the token
            int userId = extractUserIdFromToken(token);


            todoService.deleteTodoById(userId, todoId);

            return ResponseEntity.ok("Todo deleted successfully");
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not valid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete todo");
        }
    }

}
