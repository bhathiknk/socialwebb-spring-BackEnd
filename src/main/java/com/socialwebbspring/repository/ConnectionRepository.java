package com.socialwebbspring.repository;


import com.socialwebbspring.model.Connection;
import com.socialwebbspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Integer> {
    Optional<Connection> findByUser1AndUser2(User user1, User user2);
}



