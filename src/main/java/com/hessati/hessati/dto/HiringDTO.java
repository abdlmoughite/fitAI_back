package com.hessati.hessati.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HiringDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String codePostal;
    private String city;
    private String pays;
    private String tel;
    private String cvUrl;
    private String description;
}
