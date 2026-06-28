package com.hessati.hessati.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout_session")
@Getter
@Setter
public class WorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private String workoutPlanName;
    private String workoutPlanType;
    private String workoutPlanLevel;

    @CreationTimestamp
    private LocalDateTime startTime;

    private LocalDateTime endTime;
    private int durationMinutes;
    private int caloriesBurned;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.IN_PROGRESS;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionExercise> exercises = new ArrayList<>();

    public enum SessionStatus {
        IN_PROGRESS, COMPLETED, ABANDONED
    }
}
