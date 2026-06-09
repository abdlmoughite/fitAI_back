package com.hessati.hessati.authentication;

import com.hessati.hessati.requests.user.UserLoginRequest;
import com.hessati.hessati.security.config.JwtUtil;
import com.hessati.hessati.dto.UserDTO;
import com.hessati.hessati.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Validated UserLoginRequest userRequest) throws IOException {
        if(userRequest.getUsername().equals("") || userRequest.getPassword().equals("")) {
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(userRequest, userDto);
        return authenticationService.signIn(userDto);
    }

    @PostMapping("/check-token")
    public ResponseEntity<Boolean> createAuthenticationToken(@RequestBody String token) throws IOException {
        try {
            Boolean expired = !jwtUtil.isTokenExpired(token);
            return ResponseEntity.ok(expired);
        }catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

}
