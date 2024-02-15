package com.socialwebbspring.controller;

import com.socialwebbspring.model.Post;
import com.socialwebbspring.service.PostService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    // Assuming you have a service to handle business logic, inject it here
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Endpoint to get posts for a specific user
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable(name = "userId") String userId) {
        if (userId.equals("null")) {
            // Handle the case where userId is "null" (e.g., return an empty list)
            return ResponseEntity.ok(Collections.emptyList());
        }

        try {
            Integer userIdValue = Integer.parseInt(userId);
            List<Post> userPosts = postService.getPostsByUserId(userIdValue);

            // You might want to add additional error handling if userPosts is null or empty
            return ResponseEntity.ok(userPosts);
        } catch (NumberFormatException e) {
            // Handle the case where userId is not a valid integer
            return ResponseEntity.badRequest().build();
        }
    }


}