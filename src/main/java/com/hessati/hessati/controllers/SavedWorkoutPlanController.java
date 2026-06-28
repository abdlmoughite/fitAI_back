package com.hessati.hessati.controllers;

import com.hessati.hessati.services.SavedWorkoutPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SavedWorkoutPlanController {

    @Autowired
    private SavedWorkoutPlanService savedPlanService;

    @GetMapping("/user/{userId}/workout-plans")
    public ResponseEntity<?> getPlans(@PathVariable Long userId) {
        return ResponseEntity.ok(savedPlanService.getByUser(userId));
    }

    @PostMapping("/user/{userId}/workout-plan")
    public ResponseEntity<?> savePlan(@PathVariable Long userId, @RequestBody Map<String, Object> planData) {
        return ResponseEntity.ok(savedPlanService.save(userId, planData));
    }

    @DeleteMapping("/workout-plan/{planId}")
    public ResponseEntity<?> deletePlan(@PathVariable Long planId, @RequestParam Long userId) {
        boolean deleted = savedPlanService.delete(planId, userId);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
