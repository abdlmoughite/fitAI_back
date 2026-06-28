package com.hessati.hessati.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_stats")
public class UserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer totalWorkouts = 0;
    private Integer totalMinutes = 0;
    private Integer currentStreak = 0;
    private Integer bestStreak = 0;
    private Integer totalCalories = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime lastWorkoutDate;

    public void incrementWorkouts(int minutes, int calories) {
        this.totalWorkouts++;
        this.totalMinutes += minutes;
        this.totalCalories += calories;
        this.lastWorkoutDate = LocalDateTime.now();
        updateStreak();
    }

    private void updateStreak() {
        if (lastWorkoutDate == null) {
            this.currentStreak = 1;
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        long daysSinceLastWorkout = java.time.temporal.ChronoUnit.DAYS.between(lastWorkoutDate.toLocalDate(), now.toLocalDate());

        if (daysSinceLastWorkout == 1) {
            // Consecutive day
            this.currentStreak++;
        } else if (daysSinceLastWorkout > 1) {
            // Streak broken
            this.currentStreak = 1;
        }

        if (this.currentStreak > this.bestStreak) {
            this.bestStreak = this.currentStreak;
        }
    }
}
