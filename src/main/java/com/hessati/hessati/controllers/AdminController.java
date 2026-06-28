package com.hessati.hessati.controllers;

import com.hessati.hessati.entities.SupportTicket;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.repositories.SupportTicketRepository;
import com.hessati.hessati.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupportTicketRepository ticketRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        long openTickets = ticketRepository.countByStatus("open")
                + ticketRepository.countByStatus("in_progress");
        long resolvedThisWeek = ticketRepository.countByStatusAndDateAfter(
                "resolved", LocalDate.now().minusDays(7));
        long totalUsers = userRepository.count();
        long pendingUsers = userRepository.countByStatus("pending");

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("openTickets", openTickets);
        stats.put("resolvedThisWeek", resolvedThisWeek);
        stats.put("totalUsers", totalUsers);
        stats.put("pendingUsers", pendingUsers);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Map<String, Object>>> getNotifications() {
        List<Map<String, Object>> notifications = new ArrayList<>();

        // Recent open tickets as notifications
        List<SupportTicket> recentTickets = ticketRepository.findTop4ByOrderByDateDesc();
        for (SupportTicket ticket : recentTickets) {
            if ("open".equals(ticket.getStatus()) || "in_progress".equals(ticket.getStatus())) {
                Map<String, Object> notif = new LinkedHashMap<>();
                notif.put("id", ticket.getId());
                notif.put("title", "Nouveau ticket");
                notif.put("message", ticket.getUserName() + " — " + ticket.getSubject());
                notif.put("time", ticket.getDate() != null ? ticket.getDate().toString() : "Aujourd'hui");
                notif.put("read", false);
                notifications.add(notif);
            }
        }

        // Recent users as notifications
        List<User> recentUsers = userRepository.findTop5ByOrderByJoinDateDesc();
        for (User user : recentUsers) {
            Map<String, Object> notif = new LinkedHashMap<>();
            notif.put("id", 1000L + user.getId());
            notif.put("title", "Nouvel utilisateur");
            notif.put("message", user.getFirstname() + " " + user.getLastname() + " vient de s'inscrire");
            notif.put("time", user.getJoinDate() != null ? user.getJoinDate().toString() : "Récemment");
            notif.put("read", true);
            notifications.add(notif);
        }

        return ResponseEntity.ok(notifications);
    }
}
