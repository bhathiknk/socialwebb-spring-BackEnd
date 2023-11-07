package com.socialwebbspring.controller;

import com.socialwebbspring.dto.UserDetailsDto;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // Corrected: Added double quotes
public class UserDetailController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{token}")
    public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable String token) {
        User user = userService.getUserByToken(token);

        if (user != null) {
            UserDetailsDto userDetails = new UserDetailsDto(user.getUserName(), user.getEmail());
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
