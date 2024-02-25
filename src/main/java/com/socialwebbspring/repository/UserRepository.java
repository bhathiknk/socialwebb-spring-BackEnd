package com.socialwebbspring.repository;

import com.socialwebbspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    Optional<User> findById(Integer id);

    @Query("SELECT u FROM User u " +
            "WHERE u.interest = :userInterest " +
            "AND u.id NOT IN (SELECT r.receiver.id FROM ConnectionRequest r WHERE r.sender.id = :userId)")
    List<User> findSuggestedFriends(@Param("userId") Integer userId, @Param("userInterest") String userInterest);



}

