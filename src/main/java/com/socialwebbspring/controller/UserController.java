package com.socialwebbspring.controller;

import com.socialwebbspring.dto.ResponseDto;
import com.socialwebbspring.dto.SignInDto;
import com.socialwebbspring.dto.SignInResponseDto;
import com.socialwebbspring.dto.SignUpDto;
import com.socialwebbspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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




}
