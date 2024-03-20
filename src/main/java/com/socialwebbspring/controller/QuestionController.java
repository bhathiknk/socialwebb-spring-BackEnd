package com.socialwebbspring.controller;

import com.socialwebbspring.dto.CommentDTO;
import com.socialwebbspring.dto.QuestionDTO;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.Comment;
import com.socialwebbspring.model.Question;
import com.socialwebbspring.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<String> saveQuestion(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody QuestionDTO questionDTO) {
        try {
            questionService.saveQuestion(token, questionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Question saved successfully");
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/questions/friends")
    public ResponseEntity<List<Question>> getQuestionsByFriends(
            @RequestHeader(name = "Authorization") String token) {
        try {
            List<Question> questions = questionService.getQuestionsByFriends(token);
            return ResponseEntity.ok(questions); // Return questions directly
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/questions/my")
    public ResponseEntity<List<Question>> getQuestionsByUser(
            @RequestHeader(name = "Authorization") String token) {
        try {
            List<Question> questions = questionService.getQuestionsByUser(token);
            return ResponseEntity.ok(questions);
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/comments")
    public ResponseEntity<String> saveComment(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody CommentDTO commentDTO) {
        try {
            questionService.saveComment(token, commentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comment saved successfully");
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/comments/{questionId}")
    public ResponseEntity<List<Comment>> getCommentsByQuestionId(@PathVariable Integer questionId) {
        List<Comment> comments = questionService.getCommentsByQuestionId(questionId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
