package com.socialwebbspring.controller;

import com.socialwebbspring.dto.UserDetailsDto;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api") // Corrected: Added double quotes
public class UserDetailController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{token}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable String token) {
        User user = userService.getUserByToken(token);

        if (user != null) {
            UserDetailsDto userDetails = new UserDetailsDto(
                    user.getId(),
                    user.getProfileImage(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getBio()

            );
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateUserDetails(
            @PathVariable Integer id,
            @RequestParam("image") MultipartFile image,
            @RequestParam("userName") String userName,
            @RequestParam("email") String email,
            @RequestParam("text") String text) {

        try {
            userService.updateUserDetails(id, image, userName, email, text);
            return ResponseEntity.ok("User details updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating user details: " + e.getMessage());
        }
    }

    @GetMapping("/images/{imageName}")
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

        String directoryPath = "C:/Projects/Group Project Module/Social Media App-Second Year Group Project/socialwebb-spring/src/main/resources/static/images";

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

    public class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
