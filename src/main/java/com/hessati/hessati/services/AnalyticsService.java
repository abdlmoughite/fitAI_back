package com.hessati.hessati.services;

import com.hessati.hessati.entities.SessionExercise;
import com.hessati.hessati.entities.WorkoutSession;
import com.hessati.hessati.repositories.SessionExerciseRepository;
import com.hessati.hessati.repositories.WorkoutSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private WorkoutSessionRepository sessionRepository;

    @Autowired
    private SessionExerciseRepository exerciseRepository;

    private static final String[] DAY_NAMES = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
    private static final String[] MONTH_NAMES = {"Jan", "Fév", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};

    // ── Weekly summary (last 7 days) ──────────────────────────────────────────
    public Map<String, Object> getWeeklySummary(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime from = today.minusDays(6).atStartOfDay();
        LocalDateTime to = LocalDateTime.now();

        List<WorkoutSession> sessions = sessionRepository
                .findByUserIdAndStatusAndStartTimeBetween(userId, WorkoutSession.SessionStatus.COMPLETED, from, to);

        int[] calories = new int[7];
        int[] workouts = new int[7];

        for (WorkoutSession s : sessions) {
            long daysAgo = ChronoUnit.DAYS.between(s.getStartTime().toLocalDate(), today);
            if (daysAgo >= 0 && daysAgo < 7) {
                int idx = (int) (6 - daysAgo);
                calories[idx] += s.getCaloriesBurned();
                workouts[idx]++;
            }
        }

        List<Map<String, Object>> daily = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate d = today.minusDays(6 - i);
            daily.add(Map.of(
                    "day", DAY_NAMES[d.getDayOfWeek().getValue() - 1],
                    "calories", calories[i],
                    "workouts", workouts[i]
            ));
        }

        return Map.of(
                "daily", daily,
                "weeklyCalories", Arrays.stream(calories).sum(),
                "weeklyWorkouts", Arrays.stream(workouts).sum()
        );
    }

    // ── Monthly progress (last 6 months) ─────────────────────────────────────
    public Map<String, Object> getMonthlyProgress(Long userId) {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(5).withDayOfMonth(1).toLocalDate().atStartOfDay();

        List<WorkoutSession> sessions = sessionRepository
                .findByUserIdAndStatusAndStartTimeAfter(userId, WorkoutSession.SessionStatus.COMPLETED, sixMonthsAgo);

        // Build ordered keys for last 6 months
        LinkedHashMap<String, int[]> byMonth = new LinkedHashMap<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate d = LocalDate.now().minusMonths(i);
            byMonth.put(d.getYear() + "-" + d.getMonthValue(), new int[]{0, 0}); // [workouts, calories]
        }

        for (WorkoutSession s : sessions) {
            LocalDate d = s.getStartTime().toLocalDate();
            String key = d.getYear() + "-" + d.getMonthValue();
            if (byMonth.containsKey(key)) {
                byMonth.get(key)[0]++;
                byMonth.get(key)[1] += s.getCaloriesBurned();
            }
        }

        List<String> months = new ArrayList<>();
        List<Integer> workoutList = new ArrayList<>();
        List<Integer> calorieList = new ArrayList<>();

        for (int i = 5; i >= 0; i--) {
            LocalDate d = LocalDate.now().minusMonths(i);
            String key = d.getYear() + "-" + d.getMonthValue();
            months.add(MONTH_NAMES[d.getMonthValue() - 1]);
            int[] vals = byMonth.get(key);
            workoutList.add(vals[0]);
            calorieList.add(vals[1]);
        }

        return Map.of("months", months, "workouts", workoutList, "calories", calorieList);
    }

    // ── Performance par exercice ──────────────────────────────────────────────
    public List<Map<String, Object>> getPerformanceByExercise(Long userId) {
        List<SessionExercise> exercises = exerciseRepository.findCompletedByUserId(userId);

        if (exercises.isEmpty()) return Collections.emptyList();

        // Group by name
        Map<String, List<SessionExercise>> byName = exercises.stream()
                .collect(Collectors.groupingBy(SessionExercise::getName));

        return byName.entrySet().stream()
                .sorted((a, b) -> b.getValue().size() - a.getValue().size())
                .limit(6)
                .map(e -> {
                    List<SessionExercise> exList = e.getValue();
                    int planned = exList.stream().mapToInt(SessionExercise::getPlannedSets).sum();
                    int completed = exList.stream().mapToInt(SessionExercise::getCompletedSets).sum();
                    int rate = planned > 0 ? (int) Math.round((double) completed / planned * 100) : 0;
                    return Map.<String, Object>of(
                            "exercise", e.getKey(),
                            "sessions", exList.size(),
                            "plannedSets", planned,
                            "completedSets", completed,
                            "completionRate", rate
                    );
                })
                .collect(Collectors.toList());
    }

    // ── Répartition musculaire ────────────────────────────────────────────────
    public List<Map<String, Object>> getMuscleDistribution(Long userId) {
        List<SessionExercise> exercises = exerciseRepository.findCompletedByUserId(userId);

        if (exercises.isEmpty()) return defaultMuscleDistribution();

        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("Poitrine", 0);
        counts.put("Dos", 0);
        counts.put("Épaules", 0);
        counts.put("Jambes", 0);
        counts.put("Biceps", 0);
        counts.put("Triceps", 0);
        counts.put("Abdominaux", 0);

        for (SessionExercise ex : exercises) {
            String name = ex.getName().toLowerCase();
            if (name.matches(".*(pect|développé|bench|push|pompe|chest).*")) counts.merge("Poitrine", 1, Integer::sum);
            else if (name.matches(".*(dos|row|tirage|pull|soulevé|deadlift|tractions).*")) counts.merge("Dos", 1, Integer::sum);
            else if (name.matches(".*(épaul|shoulder|militaire|élévation|latéral|oiseau).*")) counts.merge("Épaules", 1, Integer::sum);
            else if (name.matches(".*(squat|jambe|fente|lunge|quad|ischios|hip|leg|rdl).*")) counts.merge("Jambes", 1, Integer::sum);
            else if (name.matches(".*(bicep|curl).*")) counts.merge("Biceps", 1, Integer::sum);
            else if (name.matches(".*(tricep|extension|dips|barre front).*")) counts.merge("Triceps", 1, Integer::sum);
            else if (name.matches(".*(abs|core|plank|gainage|crunch|twist|abdos).*")) counts.merge("Abdominaux", 1, Integer::sum);
            else counts.merge("Poitrine", 1, Integer::sum); // fallback
        }

        int total = counts.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) return defaultMuscleDistribution();

        return counts.entrySet().stream()
                .map(e -> Map.<String, Object>of(
                        "label", e.getKey(),
                        "value", (int) Math.round((double) e.getValue() / total * 100)
                ))
                .collect(Collectors.toList());
    }

    // ── Workout history ───────────────────────────────────────────────────────
    public List<Map<String, Object>> getWorkoutHistory(Long userId) {
        return sessionRepository.findByUserIdAndStatus(userId, WorkoutSession.SessionStatus.COMPLETED)
                .stream()
                .sorted(Comparator.comparing(WorkoutSession::getStartTime).reversed())
                .map(s -> Map.<String, Object>of(
                        "id", s.getId(),
                        "name", s.getWorkoutPlanName() != null ? s.getWorkoutPlanName() : "Séance",
                        "type", s.getWorkoutPlanType() != null ? s.getWorkoutPlanType() : "",
                        "date", s.getStartTime().toLocalDate().toString(),
                        "durationMinutes", s.getDurationMinutes(),
                        "caloriesBurned", s.getCaloriesBurned(),
                        "exercisesCount", s.getExercises().size()
                ))
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> defaultMuscleDistribution() {
        return List.of(
                Map.of("label", "Poitrine", "value", 0),
                Map.of("label", "Dos", "value", 0),
                Map.of("label", "Épaules", "value", 0),
                Map.of("label", "Jambes", "value", 0),
                Map.of("label", "Biceps", "value", 0),
                Map.of("label", "Triceps", "value", 0),
                Map.of("label", "Abdominaux", "value", 0)
        );
    }
}
