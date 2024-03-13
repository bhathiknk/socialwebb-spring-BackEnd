package com.socialwebbspring.service;

import com.socialwebbspring.dto.QuestionDTO;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.Question;
import com.socialwebbspring.model.User;
import com.socialwebbspring.repository.ConnectionRepository;
import com.socialwebbspring.repository.QuestionRepository;
import com.socialwebbspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveQuestion(String token, QuestionDTO questionDTO) throws AuthenticationFailException {
        // Authenticate the user
        authenticationService.authenticate(token);

        // Get the user from token
        User user = authenticationService.getUser(token);
        if (user == null) {
            throw new AuthenticationFailException("User not found");
        }

        // Create a new Question object
        Question question = new Question();
        question.setContent(questionDTO.getContent());
        question.setUser(user);

        // Save the question
        questionRepository.save(question);
    }

    public List<Question> getQuestionsByFriends(String token) throws AuthenticationFailException {
        // Extract userId from token
        User user = authenticationService.getUser(token);
        if (user == null) {
            throw new AuthenticationFailException("User not found");
        }

        // Fetch friends for the specified user
        List<User> friends = connectionRepository.findFriendsForUser1(user.getId());
        // Add the user itself to include its own questions
        friends.add(user);

        // Fetch questions posted by friends
        return questionRepository.findByUserIn(friends);
    }


}
