package com.socialwebbspring.controller;
import com.socialwebbspring.dto.UserDetailsDto;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
