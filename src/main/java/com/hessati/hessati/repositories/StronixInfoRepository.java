package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.StronixInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StronixInfoRepository extends JpaRepository<StronixInfo, Long> {
}
