package com.socialwebbspring.service;
import com.socialwebbspring.dto.UserDetailsDto;
import com.socialwebbspring.model.Connection;
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
import java.util.stream.Stream;

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


    @Transactional
    public List<UserDetailsDto> getSuggestedFriends(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String userInterest = user.getInterest();

            if (userInterest != null && !userInterest.isEmpty()) {
                // Fetch suggested friends excluding those who have pending connection requests from the logged-in user
                List<User> suggestedFriends = userRepository.findSuggestedFriends(userId, userInterest);

                // Exclude the logged-in user, users who have received a friend request from the logged-in user,
                // and users who are already friends with the logged-in user
                suggestedFriends = suggestedFriends.stream()
                        .filter(u -> !u.getId().equals(userId) && !hasReceivedFriendRequest(userId, u.getId())
                                && !areFriends(userId, u.getId()))
                        .collect(Collectors.toList());

                // Convert User entities to UserDetailsDto with profileImage
                return suggestedFriends.stream()
                        .map(u -> new UserDetailsDto(u.getId(), u.getProfileImage(), u.getUserName(), u.getEmail(), u.getBio()))
                        .collect(Collectors.toList());
            }
        }

        return Collections.emptyList();
    }
    private boolean hasReceivedFriendRequest(Integer senderId, Integer receiverId) {
        // Check if a friend request already exists in either direction
        return connectionRequestRepository.existsBySenderAndReceiver(userRepository.getById(senderId), userRepository.getById(receiverId)) ||
                connectionRequestRepository.existsBySenderAndReceiver(userRepository.getById(receiverId), userRepository.getById(senderId));
    }
    private boolean areFriends(Integer user1Id, Integer user2Id) {
        // Check if users are friends in either direction
        return connectionRepository.existsByUser1IdAndUser2Id(user1Id, user2Id) || connectionRepository.existsByUser1IdAndUser2Id(user2Id, user1Id);
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


    @Transactional
    public List<UserDetailsDto> getPendingConnectionRequests(Integer userId) {
        // Fetch pending connection requests for the specified user
        List<User> pendingRequests = connectionRequestRepository.findPendingConnectionRequests(userId);

        // Convert User entities to UserDetailsDto with profileImage
        return pendingRequests.stream()
                .map(u -> new UserDetailsDto(u.getId(), u.getProfileImage(), u.getUserName(), u.getEmail(), u.getBio()))
                .collect(Collectors.toList());
    }



    @Transactional
    public void acceptConnection(User user1, User user2) {
        // Check if a connection already exists (bidirectional check)
        if (!connectionRepository.findByUser1AndUser2(user1, user2).isPresent() &&
                !connectionRepository.findByUser1AndUser2(user2, user1).isPresent()) {

            // Create a new connection
            Connection connection = new Connection();
            connection.setUser1(user1);
            connection.setUser2(user2);
            connectionRepository.save(connection);

            // Remove the accepted connection request
            connectionRequestRepository.deleteBySenderAndReceiver(user2, user1);
        }
    }



    @Transactional
    public List<UserDetailsDto> getFriends(Integer userId) {
        List<User> friendsForUser1 = connectionRepository.findFriendsForUser1(userId);
        List<User> friendsForUser2 = connectionRepository.findFriendsForUser2(userId);

        List<User> allFriends = Stream.concat(friendsForUser1.stream(), friendsForUser2.stream())
                .distinct()
                .collect(Collectors.toList());

        return allFriends.stream()
                .map(u -> new UserDetailsDto(u.getId(), u.getProfileImage(), u.getUserName(), u.getEmail(), u.getBio()))
                .collect(Collectors.toList());
    }



}