package com.socialwebbspring.repository;

import com.socialwebbspring.model.Question;
import com.socialwebbspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findByUserIn(List<User> users);


}
