package com.hessati.hessati.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "support_ticket")
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private String userName;
    private String status = "open";
    private String priority = "medium";
    @CreationTimestamp
    private LocalDate date;
    private int messageCount = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
