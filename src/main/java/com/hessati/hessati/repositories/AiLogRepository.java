package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.AiLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiLogRepository extends JpaRepository<AiLog, Long> {

    @Query("SELECT l FROM AiLog l WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(l.userName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(l.action) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY l.timestamp DESC")
    List<AiLog> searchLogs(@Param("search") String search, Pageable pageable);
}
