package com.socialwebbspring.controller;

import com.socialwebbspring.dto.PostDto;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.Post;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.ConnectionService;
import com.socialwebbspring.service.PostService;
import com.socialwebbspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    UserService userService;

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

    @GetMapping("/friends-posts")
    public ResponseEntity<List<PostDto>> getFriendsPosts(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the authorization header
        String token = authorizationHeader.substring("Bearer ".length());

        // Authenticate the user based on the token
        User authenticatedUser = userService.getUserByToken(token);

        if (authenticatedUser != null) {
            // User is authenticated, proceed to fetch posts of friends
            List<PostDto> friendsPosts = connectionService.getFriendsPosts(authenticatedUser.getId());
            return ResponseEntity.ok(friendsPosts);
        } else {
            // User is not authenticated, return unauthorized response
            return ResponseEntity.status(401).build();
        }
    }




}