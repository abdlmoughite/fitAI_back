package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.Hiring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HiringRepository extends JpaRepository<Hiring, Long> {
}
