package com.hessati.hessati.services;

import com.hessati.hessati.entities.SupportTicket;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.repositories.SupportTicketRepository;
import com.hessati.hessati.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupportTicketService {

    @Autowired
    private SupportTicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<SupportTicket> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    public List<SupportTicket> getAll() {
        return ticketRepository.findAll();
    }

    public Optional<SupportTicket> getById(Long id) {
        return ticketRepository.findById(id);
    }

    public SupportTicket create(SupportTicket ticket, Long userId) {
        if (userId != null) {
            userRepository.findById(userId).ifPresent(user -> {
                ticket.setUser(user);
                ticket.setUserName(user.getFirstname() + " " + user.getLastname());
            });
        }
        return ticketRepository.save(ticket);
    }

    public SupportTicket updateStatus(Long id, String status) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setStatus(status);
            return ticketRepository.save(ticket);
        }).orElse(null);
    }

    public SupportTicket addMessage(Long id) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setMessageCount(ticket.getMessageCount() + 1);
            return ticketRepository.save(ticket);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
