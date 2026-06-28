package com.hessati.hessati.controllers;

import com.hessati.hessati.entities.WorkoutSession;
import com.hessati.hessati.services.WorkoutSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
public class WorkoutSessionController {

    @Autowired
    private WorkoutSessionService workoutSessionService;

    @PostMapping("/start")
    public ResponseEntity<?> startSession(@RequestBody Map<String, Object> body) {
        Long userId = ((Number) body.get("userId")).longValue();
        String planName = (String) body.getOrDefault("workoutPlanName", "");
        String planType = (String) body.getOrDefault("workoutPlanType", "");
        String planLevel = (String) body.getOrDefault("workoutPlanLevel", "");
        List<Map<String, Object>> exercises = (List<Map<String, Object>>) body.get("exercises");

        WorkoutSession session = workoutSessionService.startSession(userId, planName, planType, planLevel, exercises);
        return ResponseEntity.ok(Map.of(
                "sessionId", session.getId(),
                "startTime", session.getStartTime().toString()
        ));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeSession(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        int durationMinutes = ((Number) body.getOrDefault("durationMinutes", 0)).intValue();
        int caloriesBurned = ((Number) body.getOrDefault("caloriesBurned", 0)).intValue();
        List<Map<String, Object>> completedExercises = (List<Map<String, Object>>) body.get("completedExercises");

        Map<String, Object> result = workoutSessionService.completeSession(id, durationMinutes, caloriesBurned, completedExercises);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getSessionsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(workoutSessionService.getSessionsByUser(userId));
    }
}
