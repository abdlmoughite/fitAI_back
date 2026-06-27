package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    List<WorkoutPlan> findByGoalAndLevel(String goal, String level);
    List<WorkoutPlan> findByGoal(String goal);
}
