package com.socialwebbspring.service;
import com.socialwebbspring.dto.UserDetailsDto;
import com.socialwebbspring.model.ConnectionRequest;
import com.socialwebbspring.model.User;
import com.socialwebbspring.repository.ConnectionRepository;
import com.socialwebbspring.repository.ConnectionRequestRepository;
import com.socialwebbspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConnectionRequestRepository connectionRequestRepository ;

    // Inside ConnectionService class
    @Transactional
    public List<UserDetailsDto> getSuggestedFriends(Integer userId) {
        Optional<User> userOptional = connectionRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String userInterest = user.getInterest();

            if (userInterest != null && !userInterest.isEmpty()) {
                List<User> suggestedFriends = connectionRepository.findByInterestAndIdNot(userInterest, userId);

                // Convert User entities to UserDetailsDto with profileImage
                return suggestedFriends.stream()
                        .map(u -> new UserDetailsDto(u.getId(), u.getProfileImage(), u.getUserName(), u.getEmail(), u.getBio()))
                        .collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }


    // Inside ConnectionService.java
    @Transactional
    public boolean sendFriendRequest(Integer senderId, Integer receiverId) {
        // Check if the users are valid
        Optional<User> senderOptional = userRepository.findById(senderId);
        Optional<User> receiverOptional = userRepository.findById(receiverId);

        if (senderOptional.isPresent() && receiverOptional.isPresent()) {
            User sender = senderOptional.get();
            User receiver = receiverOptional.get();

            // Check if they are not already friends and the request doesn't exist
            if (!areFriends(sender, receiver) && !connectionRequestRepository.existsBySenderAndReceiver(sender, receiver)) {
                ConnectionRequest request = new ConnectionRequest(sender, receiver);
                connectionRequestRepository.save(request);
                return true;
            }
        }
        return false;
    }

    private boolean areFriends(User user1, User user2) {
        return connectionRequestRepository.existsBySenderAndReceiver(user1, user2) ||
                connectionRequestRepository.existsBySenderAndReceiver(user2, user1);
    }

    // Inside ConnectionService.java
    // Inside ConnectionService.java
    @Transactional
    public List<UserDetailsDto> getPendingConnectionRequests(Integer userId) {
        // Fetch pending connection requests for the specified user
        List<User> pendingRequests = connectionRequestRepository.findPendingConnectionRequests(userId);

        // Convert User entities to UserDetailsDto with profileImage
        return pendingRequests.stream()
                .map(u -> new UserDetailsDto(u.getId(), u.getProfileImage(), u.getUserName(), u.getEmail(), u.getBio()))
                .collect(Collectors.toList());
    }


}

