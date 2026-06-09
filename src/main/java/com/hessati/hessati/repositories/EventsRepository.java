package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {
    List<Events> findTop3ByOrderByIdDesc();
}
