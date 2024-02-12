package com.socialwebbspring.service;

import com.socialwebbspring.dto.PostDto;
import com.socialwebbspring.model.Post;
import com.socialwebbspring.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public void createPost(PostDto postDTO, MultipartFile imageFile) throws IOException {
        Post post = new Post();
        post.setUserId(postDTO.getUserId()); // Assuming getUserId is correct in your PostDto
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
    }
}
