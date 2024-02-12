package com.socialwebbspring.repository;

import com.socialwebbspring.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // You can add custom queries if needed
}


