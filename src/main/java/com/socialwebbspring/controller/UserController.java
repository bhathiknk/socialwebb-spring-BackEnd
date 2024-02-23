package com.socialwebbspring.controller;

import com.socialwebbspring.dto.ResponseDto;
import com.socialwebbspring.dto.SignInDto;
import com.socialwebbspring.dto.SignInResponseDto;
import com.socialwebbspring.dto.SignUpDto;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    // two apis

    // signup

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignUpDto signupDto) throws IOException {
        return userService.signUp(signupDto);
    }


    // signin

    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInDto signInDto) {
        return userService.signIn(signInDto);
    }

    @GetMapping("/suggested-friends")
    public ResponseEntity<List<User>> getSuggestedFriends(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the authorization header
        String token = authorizationHeader.substring("Bearer ".length());

        // Authenticate the user based on the token
        User authenticatedUser = userService.getUserByToken(token);

        if (authenticatedUser != null) {
            // User is authenticated, proceed to fetch suggested friends
            List<User> suggestedFriends = userService.getSuggestedFriends(authenticatedUser.getId());
            return ResponseEntity.ok(suggestedFriends);
        } else {
            // User is not authenticated, return unauthorized response
            return ResponseEntity.status(401).build();
        }
    }



}
