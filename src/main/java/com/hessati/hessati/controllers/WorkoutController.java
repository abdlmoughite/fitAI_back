package com.hessati.hessati.controllers;

import com.hessati.hessati.entities.WorkoutPlan;
import com.hessati.hessati.services.AiLogService;
import com.hessati.hessati.services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workout")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private AiLogService aiLogService;

    @PostMapping("/generate")
    public ResponseEntity<List<WorkoutPlan>> generate(@RequestBody Map<String, Object> body) {
        String goal = (String) body.getOrDefault("goal", "muscle");
        String level = (String) body.getOrDefault("level", "intermediate");
        int duration = 45;
        Object durationObj = body.get("duration");
        if (durationObj != null) {
            try {
                duration = Integer.parseInt(durationObj.toString());
            } catch (NumberFormatException ignored) {}
        }

        Long userId = null;
        String userName = "Utilisateur";
        Object userIdObj = body.get("userId");
        Object userNameObj = body.get("userName");
        if (userIdObj != null) try { userId = Long.parseLong(userIdObj.toString()); } catch (Exception ignored) {}
        if (userNameObj != null) userName = userNameObj.toString();

        long start = System.currentTimeMillis();
        List<WorkoutPlan> plans = workoutService.generateWorkouts(goal, level, duration);
        long elapsed = System.currentTimeMillis() - start;

        aiLogService.log(userId, userName, "generate_workout", 850, elapsed + "ms", "success");

        return ResponseEntity.ok(plans);
    }
}
