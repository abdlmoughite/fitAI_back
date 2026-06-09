package com.hessati.hessati.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private RoleDTO role;
    private String firstname;
    private String lastname;
    private String urlImage;
    private String email;
    private String city;
    private Long roleId;
    private String tel;

}
