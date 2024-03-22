package com.socialwebbspring.service;

import com.socialwebbspring.dto.PostDto;
import com.socialwebbspring.exceptions.AuthenticationFailException;
import com.socialwebbspring.model.AuthenticationToken;
import com.socialwebbspring.model.Post;
import com.socialwebbspring.model.User;
import com.socialwebbspring.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Transactional
    public PostDto createPost(PostDto postDTO, MultipartFile imageFile, String token) throws IOException {
        AuthenticationToken authenticationToken = authenticationService.getTokenByToken(token);
        if (authenticationToken == null) {
            throw new AuthenticationFailException("Invalid token");
        }

        Integer userId = authenticationToken.getUserIdFromToken();

        Post post = new Post();
        User user = new User();
        user.setId(userId);
        post.setUser(user);

        post.setCaption(postDTO.getCaption());
        post.setTags(postDTO.getTags());

        // Save the image file to the specified directory
        String directoryPath = "C:/Projects/Group Project Module/Social Media App-Second Year Group Project/socialwebb-spring/src/main/resources/static/posts/";
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        File destFile = new File(directoryPath + fileName);
        imageFile.transferTo(destFile);

        // Save the file name to the database
        post.setPostImage(fileName);

        // Save the post to the database
        postRepository.save(post);

        // Convert the saved post to PostDto before returning
        return convertToPostDto(post);
    }

    private PostDto convertToPostDto(Post post) {
        return new PostDto(
                post.getUser().getId(),
                post.getUser().getUserName(),
                post.getUser().getProfileImage(),
                post.getCaption(),
                post.getTags(),
                post.getPostImage()
        );
    }


    // Add a new method to retrieve posts created by the logged-in user
    public List<PostDto> getPostsByLoggedInUser(String token) {
        AuthenticationToken authenticationToken = authenticationService.getTokenByToken(token);
        if (authenticationToken == null) {
            throw new AuthenticationFailException("Invalid token");
        }

        Integer userId = authenticationToken.getUserIdFromToken();

        // Retrieve posts by the user ID
        List<Post> userPosts = postRepository.findByUserId(userId);

        // Convert Post entities to PostDto
        return userPosts.stream()
                .map(post -> {
                    PostDto postDto = convertToPostDto(post);
                    postDto.setId(post.getId()); // Set the ID field of the PostDto
                    return postDto;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void deletePost(Integer postId, String token) {
        AuthenticationToken authenticationToken = authenticationService.getTokenByToken(token);
        if (authenticationToken == null) {
            throw new AuthenticationFailException("Invalid token");
        }

        // Check if the logged-in user owns the post
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || !post.getUser().getId().equals(authenticationToken.getUserIdFromToken())) {
            throw new AuthenticationFailException("Unauthorized");
        }

        // Delete the post image from the backend saved file
        String directoryPath = "C:/Projects/Group Project Module/Social Media App-Second Year Group Project/socialwebb-spring/src/main/resources/static/posts/";
        String fileName = post.getPostImage();
        if (fileName != null) {
            File fileToDelete = new File(directoryPath + fileName);
            if (fileToDelete.exists()) {
                fileToDelete.delete();
            }
        }

        // Delete the post from the database
        postRepository.deleteById(postId);
    }

}



