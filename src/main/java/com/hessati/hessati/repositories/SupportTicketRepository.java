package com.hessati.hessati.repositories;

import com.hessati.hessati.entities.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByStatus(String status);
    List<SupportTicket> findByUserIdOrderByDateDesc(Long userId);
}
