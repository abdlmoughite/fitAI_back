package com.hessati.hessati.services;

import com.hessati.hessati.dto.UserDTO;
import com.hessati.hessati.entities.PasswordResetToken;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.entities.UserStats;
import com.hessati.hessati.repositories.PasswordResetTokenRepository;
import com.hessati.hessati.repositories.RoleRepository;
import com.hessati.hessati.repositories.UserRepository;
import com.hessati.hessati.repositories.UserStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserStatsRepository userStatsRepository;

    @Lazy
    @Autowired
    private AnalyticsService analyticsService;

    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                username,
                user.get().getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.get().getRole().getRoleName()))
        );
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User saveUser(UserDTO user) {
        User newUser = new User();
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setEmail(user.getEmail());
        newUser.setCity(user.getCity());
        newUser.setUsername(user.getUsername());
        if (user.getPassword() != null) {
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        roleRepository.findByRoleName("client").ifPresent(newUser::setRole);
        newUser.setTel(user.getTel());
        return userRepository.save(newUser);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        Pageable sortedByCreatedAt = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending()
        );
        return userRepository.findAll(sortedByCreatedAt);
    }

    public User updateUser(Long id, UserDTO user) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // FIX BUG 1: Ne jamais écraser firstname/lastname/email avec null
            if (user.getFirstname() != null) existingUser.setFirstname(user.getFirstname());
            if (user.getLastname() != null)  existingUser.setLastname(user.getLastname());
            if (user.getEmail() != null)     existingUser.setEmail(user.getEmail());
            if (user.getCity() != null)      existingUser.setCity(user.getCity());
            if (user.getTel() != null)       existingUser.setTel(user.getTel());

            // FIX BUG 1: Ne mettre à jour le username QUE si fourni (non null, non vide)
            if (user.getUsername() != null && !user.getUsername().isBlank()) {
                existingUser.setUsername(user.getUsername());
            }

            // Ne changer le password que s'il est fourni
            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            return userRepository.save(existingUser);
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean updateImageUser(Long id, MultipartFile file) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            try {
                String uploadDir = "uploads/Users/";
                String fileName = id + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
                user.setUrlImage(fileName);
                userRepository.save(user);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // FIX BUG 2: uploadProfilePicture sauvegarde réellement l'image
    public User uploadProfilePicture(Long id, MultipartFile image) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (image != null && !image.isEmpty()) {
                try {
                    String uploadDir = "uploads/Users/";
                    String fileName = id + "_" + image.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir + fileName);
                    Files.createDirectories(filePath.getParent());
                    Files.write(filePath, image.getBytes());
                    user.setUrlImage(fileName);
                    userRepository.save(user);
                } catch (IOException e) {
                    throw new RuntimeException("Impossible de sauvegarder l'image : " + e.getMessage());
                }
            }
            return userRepository.findById(id).orElse(user);
        }
        return null;
    }

    public User changePassword(Long id, String currentPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User existingUser = userOpt.get();
        if (!passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(existingUser);
    }

    public void createPasswordResetToken(String email) {
        User user = getUserByEmail(email);
        if (user == null) {

            throw new RuntimeException("User not found with email: " + email);
        }

        // Delete any existing tokens for this user
        passwordResetTokenRepository.deleteByUser(user);

        // Create new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
        passwordResetTokenRepository.save(resetToken);

        // Send email
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetTokenOpt = passwordResetTokenRepository.findByToken(token);
        if (resetTokenOpt.isEmpty()) {
            return false;
        }
        PasswordResetToken resetToken = resetTokenOpt.get();
        return !resetToken.isExpired();
    }

    public User resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> resetTokenOpt = passwordResetTokenRepository.findByToken(token);
        if (resetTokenOpt.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }
        PasswordResetToken resetToken = resetTokenOpt.get();
        if (resetToken.isExpired()) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete the token after use
        passwordResetTokenRepository.delete(resetToken);

        return user;
    }

    // Stats methods
    public UserStats getUserStats(Long userId) {
        User user = getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        UserStats stats = userStatsRepository.findByUser(user).orElse(null);
        if (stats == null) {
            stats = new UserStats();
            stats.setUser(user);
            stats = userStatsRepository.save(stats);
        }
        return stats;
    }

    public java.util.Map<String, Object> getWeeklySummary(Long userId) {
        return analyticsService.getWeeklySummary(userId);
    }

    public java.util.Map<String, Object> getMonthlyProgress(Long userId) {
        return analyticsService.getMonthlyProgress(userId);
    }

    public void recordWorkout(Long userId, int minutes, int calories) {
        User user = getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        UserStats stats = userStatsRepository.findByUser(user).orElse(null);
        if (stats == null) {
            stats = new UserStats();
            stats.setUser(user);
        }
        stats.incrementWorkouts(minutes, calories);
        userStatsRepository.save(stats);
    }

    public User updateStatus(Long id, String status) {
        return userRepository.findById(id).map(user -> {
            user.setStatus(status);
            return userRepository.save(user);
        }).orElse(null);
    }

    public User createAdmin(String firstname, String lastname, String email, String password) {
        User admin = new User();
        admin.setFirstname(firstname);
        admin.setLastname(lastname);
        admin.setEmail(email);
        admin.setUsername(email);
        admin.setPassword(passwordEncoder.encode(password));
        roleRepository.findByRoleName("admin").ifPresent(admin::setRole);
        return userRepository.save(admin);
    }
}
    