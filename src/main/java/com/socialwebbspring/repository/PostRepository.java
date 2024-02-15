package com.socialwebbspring.repository;

import com.socialwebbspring.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    // Add a new query method to find posts by user ID
    List<Post> findByUserId(Integer userId);

}


