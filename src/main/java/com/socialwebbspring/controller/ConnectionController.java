// ConnectionController.java
package com.socialwebbspring.controller;
import com.socialwebbspring.dto.PostDto;
import com.socialwebbspring.dto.UserDetailsDto;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.ConnectionService;
import com.socialwebbspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("connection")
@RestController
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    UserService userService;

    @GetMapping("/suggested-friends-details")
    public ResponseEntity<List<UserDetailsDto>> getSuggestedFriendsDetails(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the authorization header
        String token = authorizationHeader.substring("Bearer ".length());

        // Authenticate the user based on the token
        User authenticatedUser = userService.getUserByToken(token);

        if (authenticatedUser != null) {
            // User is authenticated, proceed to fetch suggested friends with details
            List<UserDetailsDto> suggestedFriends = connectionService.getSuggestedFriends(authenticatedUser.getId());
            return ResponseEntity.ok(suggestedFriends);
        } else {
            // User is not authenticated, return unauthorized response
            return ResponseEntity.status(401).build();
        }
    }



    // Inside ConnectionController.java
    @PostMapping("/send-friend-request/{friendId}")
    public ResponseEntity<String> sendFriendRequest(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer friendId) {
        // Extract the token from the authorization header
        String token = authorizationHeader.substring("Bearer ".length());

        // Authenticate the user based on the token
        User authenticatedUser = userService.getUserByToken(token);

        if (authenticatedUser != null) {
            // User is authenticated, proceed to send friend request
            boolean requestSent = connectionService.sendFriendRequest(authenticatedUser.getId(), friendId);

            if (requestSent) {
                return ResponseEntity.ok("Friend request sent successfully");
            } else {
                return ResponseEntity.badRequest().body("Friend request could not be sent");
            }
        } else {
            // User is not authenticated, return unauthorized response
            return ResponseEntity.status(401).body("User not authenticated");
        }
    }
    // Inside ConnectionController.java
    @GetMapping("/pending-connection-requests")
    public ResponseEntity<List<UserDetailsDto>> getPendingConnectionRequests(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the authorization header
        String token = authorizationHeader.substring("Bearer ".length());

        // Authenticate the user based on the token
        User authenticatedUser = userService.getUserByToken(token);

        if (authenticatedUser != null) {
            // User is authenticated, proceed to fetch pending connection requests
            List<UserDetailsDto> pendingRequests = connectionService.getPendingConnectionRequests(authenticatedUser.getId());
            return ResponseEntity.ok(pendingRequests);
        } else {
            // User is not authenticated, return unauthorized response
            return ResponseEntity.status(401).build();
        }
    }


    @PostMapping("/accept-connection/{friendId}")
    public ResponseEntity<String> acceptConnection(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Integer friendId) {
        // Extract the token from the authorization header
        String token = authorizationHeader.substring("Bearer ".length());

        // Authenticate the user based on the token
        User authenticatedUser = userService.getUserByToken(token);

        if (authenticatedUser != null) {
            // User is authenticated, proceed to accept connection
            User friendUser = userService.getUserById(friendId);

            if (friendUser != null) {
                connectionService.acceptConnection(authenticatedUser, friendUser);
                return ResponseEntity.ok("Connection accepted successfully");
            } else {
                return ResponseEntity.badRequest().body("Friend not found");
            }
        } else {
            // User is not authenticated, return unauthorized response
            return ResponseEntity.status(401).body("User not authenticated");
        }
    }


    // Inside ConnectionController.java
    @GetMapping("/friends")
    public ResponseEntity<List<UserDetailsDto>> getFriends(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the authorization header
        String token = authorizationHeader.substring("Bearer ".length());

        // Authenticate the user based on the token
        User authenticatedUser = userService.getUserByToken(token);

        if (authenticatedUser != null) {
            // User is authenticated, proceed to fetch friends
            List<UserDetailsDto> friends = connectionService.getFriends(authenticatedUser.getId());
            return ResponseEntity.ok(friends);
        } else {
            // User is not authenticated, return unauthorized response
            return ResponseEntity.status(401).build();
        }
    }


    // Inside ConnectionController.java

    @GetMapping("/pending-connection-requests-images")
    public ResponseEntity<List<String>> getPendingConnectionRequestsImages(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the authorization header
        String token = authorizationHeader.substring("Bearer ".length());

        // Authenticate the user based on the token
        User authenticatedUser = userService.getUserByToken(token);

        if (authenticatedUser != null) {
            // User is authenticated, proceed to fetch profile images of users with pending connection requests
            List<String> profileImages = connectionService.getPendingConnectionRequestsImages(authenticatedUser.getId());
            return ResponseEntity.ok(profileImages);
        } else {
            // User is not authenticated, return unauthorized response
            return ResponseEntity.status(401).build();
        }
    }

    // Inside ConnectionController.java
    @GetMapping("/friends-posts")
    public ResponseEntity<List<PostDto>> getFriendsPosts(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the authorization header
        String token = authorizationHeader.substring("Bearer ".length());

        // Authenticate the user based on the token
        User authenticatedUser = userService.getUserByToken(token);

        if (authenticatedUser != null) {
            // User is authenticated, proceed to fetch posts of friends
            List<PostDto> friendsPosts = connectionService.getFriendsPosts(authenticatedUser.getId());
            return ResponseEntity.ok(friendsPosts);
        } else {
            // User is not authenticated, return unauthorized response
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/post-images/{imageName}")
    public ResponseEntity<InputStreamResource> getPostImage(@PathVariable String imageName) {
        try {
            ClassPathResource imageFile = new ClassPathResource("static/posts/" + imageName);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)  // Adjust the MediaType based on your image type
                    .body(new InputStreamResource(imageFile.getInputStream()));
        } catch (IOException e) {
            // Handle file not found or other errors
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

}

