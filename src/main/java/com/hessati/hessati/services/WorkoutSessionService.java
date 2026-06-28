package com.hessati.hessati.services;

import com.hessati.hessati.entities.SessionExercise;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.entities.WorkoutSession;
import com.hessati.hessati.repositories.UserRepository;
import com.hessati.hessati.repositories.WorkoutSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkoutSessionService {

    @Autowired
    private WorkoutSessionRepository workoutSessionRepository;

    @Autowired
    private UserRepository userRepository;

    public WorkoutSession startSession(Long userId, String planName, String planType, String planLevel,
                                       List<Map<String, Object>> exercises) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WorkoutSession session = new WorkoutSession();
        session.setUser(user);
        session.setWorkoutPlanName(planName);
        session.setWorkoutPlanType(planType);
        session.setWorkoutPlanLevel(planLevel);
        session.setStatus(WorkoutSession.SessionStatus.IN_PROGRESS);

        List<SessionExercise> sessionExercises = exercises.stream().map(e -> {
            SessionExercise se = new SessionExercise();
            se.setSession(session);
            se.setName((String) e.get("name"));
            se.setPlannedSets(((Number) e.get("plannedSets")).intValue());
            se.setReps((String) e.getOrDefault("reps", ""));
            se.setWeight((String) e.getOrDefault("weight", "0 kg"));
            se.setCompletedSets(0);
            se.setCompleted(false);
            return se;
        }).collect(Collectors.toList());

        session.setExercises(sessionExercises);
        return workoutSessionRepository.save(session);
    }

    public Map<String, Object> completeSession(Long sessionId, int durationMinutes, int caloriesBurned,
                                               List<Map<String, Object>> completedExercises) {
        WorkoutSession session = workoutSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setEndTime(LocalDateTime.now());
        session.setDurationMinutes(durationMinutes);
        session.setCaloriesBurned(caloriesBurned);
        session.setStatus(WorkoutSession.SessionStatus.COMPLETED);

        // Update each exercise completion state
        Map<String, Map<String, Object>> completedMap = completedExercises.stream()
                .collect(Collectors.toMap(e -> (String) e.get("name"), e -> e, (a, b) -> a));

        session.getExercises().forEach(ex -> {
            Map<String, Object> done = completedMap.get(ex.getName());
            if (done != null) {
                ex.setCompletedSets(((Number) done.getOrDefault("completedSets", ex.getPlannedSets())).intValue());
                ex.setCompleted((Boolean) done.getOrDefault("completed", false));
            }
        });

        workoutSessionRepository.save(session);

        // Update user stats on the User entity (used by frontend)
        User user = session.getUser();
        user.setTotalWorkouts(user.getTotalWorkouts() + 1);
        user.setTotalMinutes(user.getTotalMinutes() + durationMinutes);
        user.setCurrentStreak(user.getCurrentStreak() + 1);
        if (user.getCurrentStreak() > user.getBestStreak()) {
            user.setBestStreak(user.getCurrentStreak());
        }
        User savedUser = userRepository.save(user);

        return Map.of(
                "sessionId", session.getId(),
                "user", savedUser
        );
    }

    public List<Map<String, Object>> getSessionsByUser(Long userId) {
        return workoutSessionRepository.findByUserIdOrderByStartTimeDesc(userId).stream()
                .map(s -> Map.<String, Object>of(
                        "id", s.getId(),
                        "workoutPlanName", s.getWorkoutPlanName() != null ? s.getWorkoutPlanName() : "",
                        "workoutPlanType", s.getWorkoutPlanType() != null ? s.getWorkoutPlanType() : "",
                        "workoutPlanLevel", s.getWorkoutPlanLevel() != null ? s.getWorkoutPlanLevel() : "",
                        "startTime", s.getStartTime() != null ? s.getStartTime().toString() : "",
                        "durationMinutes", s.getDurationMinutes(),
                        "caloriesBurned", s.getCaloriesBurned(),
                        "status", s.getStatus().name(),
                        "exercisesCount", s.getExercises().size()
                ))
                .collect(Collectors.toList());
    }
}
