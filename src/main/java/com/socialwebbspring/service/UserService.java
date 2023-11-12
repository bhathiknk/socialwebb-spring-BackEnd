package com.socialwebbspring.service;


import com.socialwebbspring.dto.*;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.exceptions.CustomException;
import com.socialwebbspring.model.AuthenticationToken;
import com.socialwebbspring.model.User;
import com.socialwebbspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    @Autowired
    AuthenticationService authenticationService;


    @Transactional
    public ResponseDto signUp(SignUpDto signupDto) {
        // check if user is already present
        if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
            // we have an user
            throw new CustomException("User already present");
        }

        // hash the password

        String encryptedpassword = signupDto.getPassword();

        try {
            encryptedpassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        User user = new User(signupDto.getFirstName(), signupDto.getLastName(),
                signupDto.getEmail(), signupDto.getUserName(),signupDto.getInterest(),encryptedpassword, signupDto.getProfileImage(), signupDto.getText());

        userRepository.save(user);

        // save the user

        // create the token

        final AuthenticationToken authenticationToken = new AuthenticationToken(user);

        authenticationService.saveConfirmationToken(authenticationToken);

        ResponseDto responseDto = new ResponseDto("success", "user created succesfully");
        return responseDto;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return hash;
    }

    public SignInResponseDto signIn(SignInDto signInDto) {
        // find user by email

        User user = userRepository.findByEmail(signInDto.getEmail());

        if (Objects.isNull(user)) {
            throw new AuthenticationFailException("user is not valid");
        }

        // hash the password

        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AuthenticationFailException("wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // compare the password in DB

        // if password match

        AuthenticationToken token = authenticationService.getToken(user);

        // retrive the token

        if (Objects.isNull(token)) {
            throw new CustomException("token is not present");
        }

        return new SignInResponseDto("sucess", token.getToken());

        // return response
    }
    public User getUserByToken(String token) {
        authenticationService.authenticate(token); // Ensure the token is valid
        return authenticationService.getUser(token);
    }

    public void updateUser(UserDetailsDto userDetailsDto, Integer id) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);
        // throw an exception if product does not exists
        if (!optionalUser.isPresent()) {
            throw new Exception("user not present");
        }
        User user = optionalUser.get();
        user.setUserName(userDetailsDto.getUserName());
        user.setEmail(userDetailsDto.getEmail());
        user.setText(userDetailsDto.getText());
        userRepository.save(user);
    }

}