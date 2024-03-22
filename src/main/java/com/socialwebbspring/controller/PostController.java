package com.socialwebbspring.controller;

import com.socialwebbspring.dto.PostDto;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.Post;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.ConnectionService;
import com.socialwebbspring.service.PostService;
import com.socialwebbspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public ResponseEntity<PostDto> createPost(@ModelAttribute PostDto postDTO,
                                              @RequestParam("image") MultipartFile imageFile,
                                              @RequestHeader("Authorization") String token) {
        try {
            PostDto createdPost = postService.createPost(postDTO, imageFile, token);
            return ResponseEntity.ok(createdPost);
        } catch (IOException | AuthenticationFailException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/friends-posts")
    public ResponseEntity<List<PostDto>> getFriendsPosts(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the authorization header
        System.out.println("Authorization Header: " + authorizationHeader);
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


    @GetMapping("/postImages/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
        // Assuming you have a method to retrieve the image file as InputStream
        InputStream imageStream = getImageInputStream(imageName);

        if (imageStream != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic()); // Cache image for 1 day
            headers.setExpires(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(imageStream));
        } else {
            // Handle case where image is not found
            return ResponseEntity.notFound().build();
        }
    }

    // Method to retrieve the image file as InputStream
    private InputStream getImageInputStream(String imageName) throws IOException {

        String directoryPath = "C:/Projects/Group Project Module/Social Media App-Second Year Group Project/socialwebb-spring/src/main/resources/static/posts";

        // Use Paths.get to create a Path object
        Path imagePath = Paths.get(directoryPath, imageName);

        // Check if the file exists
        if (Files.exists(imagePath)) {
            // Use Files.newInputStream to get the InputStream
            return Files.newInputStream(imagePath);
        } else {
            // Return null if the image is not found
            return null;
        }
    }

    @GetMapping("/user/my-posts")
    public ResponseEntity<List<PostDto>> getMyPosts(@RequestHeader("Authorization") String token) {
        try {
            List<PostDto> myPosts = postService.getPostsByLoggedInUser(token);
            return ResponseEntity.ok(myPosts);
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(401).body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Integer postId, @RequestHeader("Authorization") String token) {
        try {
            postService.deletePost(postId, token);
            return ResponseEntity.ok("Post deleted successfully");
        } catch (AuthenticationFailException e) {
            return ResponseEntity.status(401).body("Unauthorized");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }


}