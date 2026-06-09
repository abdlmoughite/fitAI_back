package com.hessati.hessati.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "references_stronix")
@Entity
@Getter
@Setter
public class References {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String name;
    @Lob
    private String description;
    @ManyToMany
    @JoinTable(
            name = "references_images",
            joinColumns = @JoinColumn(name = "references_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private List<Images> images = new ArrayList<>();
}
