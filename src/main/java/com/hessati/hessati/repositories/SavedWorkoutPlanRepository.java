package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.SavedWorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedWorkoutPlanRepository extends JpaRepository<SavedWorkoutPlan, Long> {
    List<SavedWorkoutPlan> findByUserIdOrderBySavedAtDesc(Long userId);
}
