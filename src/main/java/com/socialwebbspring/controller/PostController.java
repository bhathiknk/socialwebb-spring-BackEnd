package com.socialwebbspring.controller;

import com.socialwebbspring.dto.PostDto;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@ModelAttribute PostDto postDTO,
                                             @RequestParam("image") MultipartFile imageFile,
                                             @RequestHeader("Authorization") String token) {
        try {
            postService.createPost(postDTO, imageFile, token);
            return ResponseEntity.ok("Post created successfully");
        } catch (IOException | AuthenticationFailException e) {
            return ResponseEntity.status(500).body("Error creating post: " + e.getMessage());
        }
    }



}