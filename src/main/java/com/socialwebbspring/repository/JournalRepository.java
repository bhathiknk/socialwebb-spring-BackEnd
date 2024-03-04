package com.socialwebbspring.repository;

import com.socialwebbspring.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JournalRepository.java
public interface JournalRepository extends JpaRepository<Journal, Integer> {
    List<Journal> findByUserId(Integer userId);
}
