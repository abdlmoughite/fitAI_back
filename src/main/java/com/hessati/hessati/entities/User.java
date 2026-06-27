package com.hessati.hessati.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    private String firstname;
    private String lastname;
    private String urlImage;
    private String email;
    private String city;
    private String tel;
    private String plan = "Starter";
    private int totalWorkouts = 0;
    private int totalMinutes = 0;
    private int currentStreak = 0;
    private int bestStreak = 0;
    @CreationTimestamp
    private LocalDate joinDate;
}
