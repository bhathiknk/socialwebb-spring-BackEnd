package com.socialwebbspring.service;
import com.socialwebbspring.dto.UserDetailsDto;
import com.socialwebbspring.model.User;
import com.socialwebbspring.repository.ConnectionRepository;
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


}

