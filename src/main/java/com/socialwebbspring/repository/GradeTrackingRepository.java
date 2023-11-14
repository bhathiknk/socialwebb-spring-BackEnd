package com.socialwebbspring.repository;

import com.socialwebbspring.model.GradeTracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeTrackingRepository extends JpaRepository<GradeTracking, Long> {
    // Custom queries if needed
}
