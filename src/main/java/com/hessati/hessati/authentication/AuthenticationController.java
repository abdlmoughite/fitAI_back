package com.hessati.hessati.authentication;

import com.hessati.hessati.requests.user.UserLoginRequest;
import com.hessati.hessati.security.config.JwtUtil;
import com.hessati.hessati.dto.UserDTO;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

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
        if (userRequest.getUsername() == null || userRequest.getUsername().isBlank()
                || userRequest.getPassword() == null || userRequest.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
        }
        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(userRequest, userDto);
        return authenticationService.signIn(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String firstname = body.getOrDefault("firstname", "");
        String lastname = body.getOrDefault("lastname", "");
        String email = body.getOrDefault("email", "");
        String password = body.getOrDefault("password", "");

        if (email.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
        }

        if (userService.getUserByEmail(email) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already in use"));
        }

        UserDTO userDto = new UserDTO();
        userDto.setFirstname(firstname);
        userDto.setLastname(lastname);
        userDto.setEmail(email);
        userDto.setUsername(email);
        userDto.setPassword(password);

        User saved = userService.saveUser(userDto);
        if (saved == null) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Registration failed"));
        }

        // Auto-login after register
        UserDTO loginDto = new UserDTO();
        loginDto.setUsername(email);
        loginDto.setPassword(password);
        return authenticationService.signIn(loginDto);
    }

    @PostMapping("/check-token")
    public ResponseEntity<Boolean> checkToken(@RequestBody String token) throws IOException {
        try {
            Boolean valid = !jwtUtil.isTokenExpired(token);
            return ResponseEntity.ok(valid);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}
