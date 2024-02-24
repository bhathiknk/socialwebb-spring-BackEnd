package com.socialwebbspring.service;

import com.socialwebbspring.dto.*;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.exceptions.CustomException;
import com.socialwebbspring.model.AuthenticationToken;
import com.socialwebbspring.model.User;
import com.socialwebbspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    // Signup method
    @Transactional
    public ResponseDto signUp(SignUpDto signupDto) {
        // Check if user is already present
        if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
            // User already exists
            throw new CustomException("User already present");
        }

        // Hash the password
        String encryptedpassword = signupDto.getPassword();
        try {
            encryptedpassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Create a new user
        User user = new User(signupDto.getFirstName(), signupDto.getLastName(),
                signupDto.getEmail(), signupDto.getUserName(), signupDto.getInterest(), encryptedpassword, signupDto.getText());

        userRepository.save(user);  // Save the user to the repository

        // Create authentication token
        final AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        ResponseDto responseDto = new ResponseDto("success", "User created successfully");
        return responseDto;
    }

    // Password encrypt method
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return hash;
    }

    // Sign in method
    public SignInResponseDto signIn(SignInDto signInDto) {
        // Find user by email
        User user = userRepository.findByEmail(signInDto.getEmail());

        if (Objects.isNull(user)) {
            throw new AuthenticationFailException("User is not valid");
        }

        // Check hashed password
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AuthenticationFailException("Wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Retrieve authentication token
        AuthenticationToken token = authenticationService.getToken(user);

        if (Objects.isNull(token)) {
            throw new CustomException("Token is not present");
        }

        return new SignInResponseDto("success", token.getToken());
    }

    // Check user token
    public User getUserByToken(String token) {
        authenticationService.authenticate(token); // Ensure the token is valid
        return authenticationService.getUser(token);
    }

    // Update user details method
    @Transactional
    public void updateUserDetails(Integer id, MultipartFile image, String userName, String email, String text) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new Exception("User not found");
        }

        User user = optionalUser.get();
        String oldProfileImage = user.getProfileImage(); // Get the old image filename

        // Update user details
        user.setUserName(userName);
        user.setEmail(email);
        user.setBio(text);

        if (image != null && !image.isEmpty()) {
            // Save the new image
            String newImageName = saveImage(image);
            user.setProfileImage(newImageName);

            // Remove the old image
            if (oldProfileImage != null && !oldProfileImage.isEmpty()) {
                removeImage(oldProfileImage);
            }
        }

        userRepository.save(user);
    }

    // Remove the old image
    private void removeImage(String imageName) throws IOException {
        String uploadDir = "C:/Projects/Group Project Module/Social Media App-Second Year Group Project/socialwebb-spring/src/main/resources/static/images";
        Path filePath = Path.of(uploadDir, imageName);
        Files.deleteIfExists(filePath);
    }

    // Save image to a separate directory
    private String saveImage(MultipartFile image) throws IOException {
        // Customize the directory to save the images
        String uploadDir = "C:/Projects/Group Project Module/Social Media App-Second Year Group Project/socialwebb-spring/src/main/resources/static/images";
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path filePath = Path.of(uploadDir, fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }


    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

}