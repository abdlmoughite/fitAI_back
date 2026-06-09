package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.Product;
import com.hessati.hessati.entities.References;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferencesRepository extends JpaRepository<References, Long> {
    List<References> findTop3ByOrderByIdDesc();
}
