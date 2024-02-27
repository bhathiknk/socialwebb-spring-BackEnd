package com.socialwebbspring.controller;

import com.socialwebbspring.dto.PostDto;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.Post;
import com.socialwebbspring.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
// call the create post frontend
    public PostController(PostService postService) {
        this.postService = postService;
    }
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

    // Endpoint to get posts for a specific user
    // Add a new method to get posts by user token
    @GetMapping("/user/posts")
    public ResponseEntity<List<Post>> getUserPostsByToken(@RequestHeader("Authorization") String token) {
        try {
            List<Post> userPosts = postService.getPostsByUserToken(token);
            return ResponseEntity.ok(userPosts);
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(401).body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }






}