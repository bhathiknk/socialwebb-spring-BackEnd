package com.socialwebbspring.controller;

import com.socialwebbspring.dto.UserDetailsDto;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api") // Corrected: Added double quotes
public class UserDetailController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{token}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable String token) {
        User user = userService.getUserByToken(token);

        if (user != null) {
            UserDetailsDto userDetails = new UserDetailsDto(user.getUserName(), user.getEmail(),user.getId(), user.getText());
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("id") Integer id, @RequestBody UserDetailsDto userDetailsDto) throws Exception {

        userService.updateUser(userDetailsDto, id);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "User Details has been updated"), HttpStatus.OK);
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
