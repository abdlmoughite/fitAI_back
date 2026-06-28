package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.SessionExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SessionExerciseRepository extends JpaRepository<SessionExercise, Long> {

    @Query("SELECT se FROM SessionExercise se WHERE se.session.user.id = :userId AND se.session.status = 'COMPLETED'")
    List<SessionExercise> findCompletedByUserId(@Param("userId") Long userId);
}
