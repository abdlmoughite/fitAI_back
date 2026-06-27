package com.hessati.hessati.controllers;

import com.hessati.hessati.entities.SupportTicket;
import com.hessati.hessati.services.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class SupportTicketController {

    @Autowired
    private SupportTicketService ticketService;

    @GetMapping
    public ResponseEntity<List<SupportTicket>> getAll() {
        return ResponseEntity.ok(ticketService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicket> getById(@PathVariable Long id) {
        return ticketService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SupportTicket> create(@RequestBody Map<String, Object> body) {
        SupportTicket ticket = new SupportTicket();
        ticket.setSubject((String) body.getOrDefault("subject", "Nouveau ticket"));
        ticket.setPriority((String) body.getOrDefault("priority", "medium"));
        ticket.setStatus("open");
        ticket.setUserName((String) body.getOrDefault("userName", "Utilisateur"));

        Long userId = null;
        Object userIdObj = body.get("userId");
        if (userIdObj != null) {
            try { userId = Long.parseLong(userIdObj.toString()); } catch (NumberFormatException ignored) {}
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.create(ticket, userId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SupportTicket> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        SupportTicket updated = ticketService.updateStatus(id, status);
        if (updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/message")
    public ResponseEntity<SupportTicket> addMessage(@PathVariable Long id) {
        SupportTicket updated = ticketService.addMessage(id);
        if (updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.delete(id));
    }
}
