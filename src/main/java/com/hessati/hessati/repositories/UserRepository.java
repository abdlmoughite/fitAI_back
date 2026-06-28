package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    long countByStatus(String status);
    long countByRoleRoleName(String roleName);
    List<User> findTop5ByOrderByJoinDateDesc();
}
