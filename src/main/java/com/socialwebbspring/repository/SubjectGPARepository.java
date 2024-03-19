package com.socialwebbspring.repository;

import com.socialwebbspring.model.SubjectGPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectGPARepository extends JpaRepository<SubjectGPA, Integer> {

    List<SubjectGPA> findByUserId(int userId);
}
