package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop3ByOrderByIdDesc();
    List<Product> findTop3ByCategory_NameOrderByIdDesc(String category);
    Page<Product> findByCategory_NameOrderByIdDesc(String category, Pageable pageable);
}
