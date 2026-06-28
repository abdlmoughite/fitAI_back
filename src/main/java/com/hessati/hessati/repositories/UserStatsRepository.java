package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.UserStats;
import com.hessati.hessati.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    Optional<UserStats> findByUser(User user);
}
