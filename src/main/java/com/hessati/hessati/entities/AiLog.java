package com.hessati.hessati.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "ai_log")
public class AiLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String userName;
    private String action;
    private String model = "FitAI Engine";
    private int tokens = 0;
    private String latency = "0ms";
    private String status = "success";

    @CreationTimestamp
    private LocalDateTime timestamp;
}
