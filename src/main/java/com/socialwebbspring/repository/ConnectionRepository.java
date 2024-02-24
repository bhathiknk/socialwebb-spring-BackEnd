package com.socialwebbspring.repository;

import com.socialwebbspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<User, Integer> {
    
    List<User> findByInterestAndIdNot(String userInterest, Integer userId);


}
