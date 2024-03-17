package com.socialwebbspring.repository;


import com.socialwebbspring.model.ModuleQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleQuestionRepository extends JpaRepository<ModuleQuestion, Integer> {

    List<ModuleQuestion> findByUserIdAndModuleId(Integer userId, Integer moduleId);
}
