package com.socialwebbspring.repository;

import com.socialwebbspring.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    // Add a new query method to find posts by user ID
    List<Post> findByUserId(Integer userId);
    @Query("SELECT p FROM Post p WHERE p.user.id IN :userIds")
    List<Post> findByUserIds(@Param("userIds") List<Integer> userIds);
}


