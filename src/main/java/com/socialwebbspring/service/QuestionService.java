package com.socialwebbspring.service;

import com.socialwebbspring.dto.CommentDTO;
import com.socialwebbspring.dto.QuestionDTO;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.Comment;
import com.socialwebbspring.model.Question;
import com.socialwebbspring.model.User;
import com.socialwebbspring.repository.CommentRepository;
import com.socialwebbspring.repository.ConnectionRepository;
import com.socialwebbspring.repository.QuestionRepository;
import com.socialwebbspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private CommentRepository commentRepository;


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

    public void saveComment(String token, CommentDTO commentDTO) throws AuthenticationFailException {
        // Authenticate the user
        authenticationService.authenticate(token);

        // Get the user from token
        User user = authenticationService.getUser(token);
        if (user == null) {
            throw new AuthenticationFailException("User not found");
        }

        // Create a new Comment object
        Comment comment = new Comment();
        comment.setQuestionId(commentDTO.getQuestionId());
        comment.setUser(user);
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        // Save the comment
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByQuestionId(Integer questionId) {
        List<Comment> comments = commentRepository.findByQuestionId(questionId);
        return comments;
    }
}
