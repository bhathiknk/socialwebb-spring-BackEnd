package com.socialwebbspring.repository;

import com.socialwebbspring.model.ConnectionRequest;
import com.socialwebbspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// ConnectionRequestRepository.java
@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Integer> {

    boolean existsBySenderAndReceiver(User sender, User receiver);
    @Query("SELECT r.sender FROM ConnectionRequest r WHERE r.receiver.id = :userId")
    List<User> findPendingConnectionRequests(@Param("userId") Integer userId);


    @Query("SELECT r.receiver.profileImage FROM ConnectionRequest r WHERE r.sender.id = :userId")
    List<String> findSentFriendRequestsImages(@Param("userId") Integer userId);


    void deleteBySenderAndReceiver(User sender, User receiver);
}