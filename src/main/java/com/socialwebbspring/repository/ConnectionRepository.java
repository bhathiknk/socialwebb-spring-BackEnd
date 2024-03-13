package com.socialwebbspring.repository;


import com.socialwebbspring.model.Connection;
import com.socialwebbspring.model.Question;
import com.socialwebbspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Integer> {
    Optional<Connection> findByUser1AndUser2(User user1, User user2);

    @Query("SELECT c.user1 FROM Connection c WHERE c.user2.id = :userId")
    List<User> findFriendsForUser2(@Param("userId") Integer userId);

    @Query("SELECT c.user2 FROM Connection c WHERE c.user1.id = :userId")
    List<User> findFriendsForUser1(@Param("userId") Integer userId);


    boolean existsByUser1IdAndUser2Id(Integer user1Id, Integer user2Id);
    @Query("SELECT c.user2.id FROM Connection c WHERE c.user1.id = :userId")
    List<Integer> findFriendIdsByUserId(@Param("userId") Integer userId);


}