package com.hessati.hessati.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReferencesDTO {
    private Long id;
    private String name;
    private String description;
    private List<String> urlImage;
}
