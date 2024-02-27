package com.socialwebbspring.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@RequestMapping("image")
@RestController
public class ProfileFeedImageController {



    @GetMapping("/post-images/{imageName}")
    public ResponseEntity<InputStreamResource> getPostImage(@PathVariable String imageName) {
        try {
            ClassPathResource imageFile = new ClassPathResource("static/posts/" + imageName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)  // Adjust the MediaType based on your image type
                    .body(new InputStreamResource(imageFile.getInputStream()));
        } catch (IOException e) {
            // Handle file not found or other errors
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }
    @GetMapping("/profile-images/{imageName}")
    public ResponseEntity<InputStreamResource> getProfileImage(@PathVariable String imageName) {
        try {
            ClassPathResource imageFile = new ClassPathResource("static/images/" + imageName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)  // Adjust the MediaType based on your image type
                    .body(new InputStreamResource(imageFile.getInputStream()));
        } catch (IOException e) {
            // Handle file not found or other errors
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }
}
