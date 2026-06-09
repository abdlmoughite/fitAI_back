package com.hessati.hessati.authentication;

import com.hessati.hessati.security.config.JwtUtil;
import com.hessati.hessati.dto.UserDTO;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<?> signIn(UserDTO userDto){
        Map<String, Object> responseToken = new HashMap<>();
        final UserDetails user = userService.loadUserByUsername(userDto.getUsername());
        if(user != null) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );
            User userInfo = userService.getUserByUsername(userDto.getUsername());
            responseToken.put("accessToken", jwtUtil.generateToken(user));
            responseToken.put("user", userInfo);
            return ResponseEntity.ok(responseToken);
        }
        responseToken.put("accessToken", null);
        responseToken.put("user", null);
        return ResponseEntity.status(400).body(responseToken);
    }

}
