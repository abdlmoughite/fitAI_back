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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }

        try {
            userService.createPasswordResetToken(email);
            return ResponseEntity.ok(Map.of("message", "Password reset email sent"));
        } catch (RuntimeException e) {
            // For security, don't reveal if email exists or not
            return ResponseEntity.ok(Map.of("message", "If the email exists, a reset link has been sent"));
        }
    }

    @PostMapping("/validate-reset-token")
    public ResponseEntity<Boolean> validateResetToken(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || token.isBlank()) {
            return ResponseEntity.ok(false);
        }

        boolean valid = userService.validatePasswordResetToken(token);
        return ResponseEntity.ok(valid);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        if (token == null || token.isBlank() || newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token and new password are required"));
        }

        try {
            User user = userService.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
