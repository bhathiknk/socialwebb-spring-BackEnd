package com.socialwebbspring.repository;

import com.socialwebbspring.model.ConnectionRequest;
import com.socialwebbspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ConnectionRequestRepository.java
@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Integer> {

    boolean existsBySenderAndReceiver(User sender, User receiver);
}
