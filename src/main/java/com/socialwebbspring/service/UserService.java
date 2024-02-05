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
import org.springframework.web.multipart.MultipartFile;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    @Autowired
    AuthenticationService authenticationService;


    //Signup method
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
                signupDto.getEmail(), signupDto.getUserName(), signupDto.getInterest(), encryptedpassword, signupDto.getText());

        userRepository.save(user);

        // save the user

        // create the token

        final AuthenticationToken authenticationToken = new AuthenticationToken(user);

        authenticationService.saveConfirmationToken(authenticationToken);

        ResponseDto responseDto = new ResponseDto("success", "User created successfully");
        return responseDto;
    }






    //password encript method
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return hash;
    }






    //signin method
    public SignInResponseDto signIn(SignInDto signInDto) {
        // find user by email

        User user = userRepository.findByEmail(signInDto.getEmail());

        if (Objects.isNull(user)) {
            throw new AuthenticationFailException("user is not valid");
        }

        // check hash password
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



    //check user token
    public User getUserByToken(String token) {
        authenticationService.authenticate(token); // Ensure the token is valid
        return authenticationService.getUser(token);
    }




    @Transactional
    public void updateUserDetails(Integer id, MultipartFile image, String userName, String email, String text) throws Exception {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new Exception("User not found");
        }

        User user = optionalUser.get();
        String oldProfileImage = user.getProfileImage(); // Get the old image filename

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

    // Add a method to remove the old image
    private void removeImage(String imageName) throws IOException {
        String uploadDir = "C:/Projects/Group Project Module/Social Media App-Second Year Group Project/socialwebb-spring/src/main/resources/static/images";
        Path filePath = Path.of(uploadDir, imageName);
        Files.deleteIfExists(filePath);
    }




    //Save image seperate directory
    private String saveImage(MultipartFile image) throws IOException {
        // You can customize the directory to save the images
        String uploadDir = "C:/Projects/Group Project Module/Social Media App-Second Year Group Project/socialwebb-spring/src/main/resources/static/images";
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path filePath = Path.of(uploadDir, fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }



}
