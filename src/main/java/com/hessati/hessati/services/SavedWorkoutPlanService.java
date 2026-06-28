package com.hessati.hessati.services;

import com.hessati.hessati.entities.SavedWorkoutExercise;
import com.hessati.hessati.entities.SavedWorkoutPlan;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.repositories.SavedWorkoutPlanRepository;
import com.hessati.hessati.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SavedWorkoutPlanService {

    @Autowired
    private SavedWorkoutPlanRepository savedPlanRepository;

    @Autowired
    private UserRepository userRepository;

    public SavedWorkoutPlan save(Long userId, Map<String, Object> planData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SavedWorkoutPlan plan = new SavedWorkoutPlan();
        plan.setUser(user);
        plan.setName((String) planData.getOrDefault("name", ""));
        plan.setType((String) planData.getOrDefault("type", ""));
        plan.setLevel((String) planData.getOrDefault("level", ""));
        plan.setDuration(((Number) planData.getOrDefault("duration", 0)).intValue());
        plan.setSessions(((Number) planData.getOrDefault("sessions", 0)).intValue());
        plan.setCalories(((Number) planData.getOrDefault("calories", 0)).intValue());
        plan.setGoal((String) planData.getOrDefault("goal", ""));

        List<Map<String, Object>> exercises = (List<Map<String, Object>>) planData.get("exercises");
        if (exercises != null) {
            List<SavedWorkoutExercise> exList = exercises.stream().map(e -> {
                SavedWorkoutExercise ex = new SavedWorkoutExercise();
                ex.setPlan(plan);
                ex.setName((String) e.getOrDefault("name", ""));
                ex.setSets(((Number) e.getOrDefault("sets", 0)).intValue());
                ex.setReps((String) e.getOrDefault("reps", ""));
                ex.setRest((String) e.getOrDefault("rest", "60s"));
                return ex;
            }).collect(Collectors.toList());
            plan.setExercises(exList);
        }

        return savedPlanRepository.save(plan);
    }

    public List<Map<String, Object>> getByUser(Long userId) {
        return savedPlanRepository.findByUserIdOrderBySavedAtDesc(userId).stream()
                .map(p -> {
                    Map<String, Object> result = new java.util.HashMap<>();
                    result.put("id", p.getId());
                    result.put("name", p.getName());
                    result.put("type", p.getType());
                    result.put("level", p.getLevel());
                    result.put("duration", p.getDuration());
                    result.put("sessions", p.getSessions());
                    result.put("calories", p.getCalories());
                    result.put("goal", p.getGoal());
                    result.put("savedAt", p.getSavedAt() != null ? p.getSavedAt().toLocalDate().toString() : "");
                    result.put("exercises", p.getExercises().stream().map(e -> Map.of(
                            "name", e.getName(),
                            "sets", e.getSets(),
                            "reps", e.getReps(),
                            "rest", e.getRest()
                    )).collect(Collectors.toList()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    public boolean delete(Long planId, Long userId) {
        return savedPlanRepository.findById(planId).map(p -> {
            if (!p.getUser().getId().equals(userId)) return false;
            savedPlanRepository.delete(p);
            return true;
        }).orElse(false);
    }
}
