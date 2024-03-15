package com.socialwebbspring.repository;


import com.socialwebbspring.model.ModuleQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleQuestionRepository extends JpaRepository<ModuleQuestion, Integer> {
}
