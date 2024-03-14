package com.socialwebbspring.repository;


import com.socialwebbspring.dto.ModuleDTO;
import com.socialwebbspring.model.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, Integer> {

    List<ModuleEntity> findByUserId(Integer userId);
}
