package com.hessati.hessati.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "session_exercise")
@Getter
@Setter
public class SessionExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    @JsonIgnore
    private WorkoutSession session;

    private String name;
    private int plannedSets;
    private int completedSets;
    private String reps;
    private String weight;
    private boolean completed;
}
