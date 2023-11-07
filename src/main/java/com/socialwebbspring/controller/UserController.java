package com.socialwebbspring.controller;

import com.socialwebbspring.dto.ResponseDto;
import com.socialwebbspring.dto.SignInDto;
import com.socialwebbspring.dto.SignInResponseDto;
import com.socialwebbspring.dto.SignUpDto;
import com.socialwebbspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    // two apis

    // signup

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignUpDto signupDto) {
        return userService.signUp(signupDto);
    }


    // signin

    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInDto signInDto) {
        return userService.signIn(signInDto);
    }




}
