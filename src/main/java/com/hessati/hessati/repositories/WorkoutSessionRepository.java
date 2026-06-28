package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
    List<WorkoutSession> findByUserIdOrderByStartTimeDesc(Long userId);
    List<WorkoutSession> findByUserIdAndStatus(Long userId, WorkoutSession.SessionStatus status);
    List<WorkoutSession> findByUserIdAndStatusAndStartTimeAfter(Long userId, WorkoutSession.SessionStatus status, LocalDateTime after);
    List<WorkoutSession> findByUserIdAndStatusAndStartTimeBetween(Long userId, WorkoutSession.SessionStatus status, LocalDateTime from, LocalDateTime to);
}
