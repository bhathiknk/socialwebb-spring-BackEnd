// ConnectionController.java
package com.socialwebbspring.controller;
import com.socialwebbspring.dto.UserDetailsDto;
import com.socialwebbspring.model.User;
import com.socialwebbspring.service.ConnectionService;
import com.socialwebbspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}

