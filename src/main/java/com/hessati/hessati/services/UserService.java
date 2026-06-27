package com.hessati.hessati.services;

import com.hessati.hessati.dto.UserDTO;
import com.hessati.hessati.entities.Role;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.repositories.RoleRepository;
import com.hessati.hessati.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

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
        if(user.getPassword() != null) {
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        roleRepository.findByRoleName("client").ifPresent(newUser::setRole);
        newUser.setTel(user.getTel());
        return userRepository.save(newUser);
    }

    public User getUserById(Long id) {
        Optional<User> UserOptional = userRepository.findById(id);
        return UserOptional.orElse(null);
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
            existingUser.setFirstname(user.getFirstname());
            existingUser.setLastname(user.getLastname());
            existingUser.setEmail(user.getEmail());
            existingUser.setCity(user.getCity());
            existingUser.setUsername(user.getUsername());
            if(user.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            existingUser.setTel(user.getTel());
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
        Optional<User> UserOptional = userRepository.findById(id);
        if (UserOptional.isPresent()) {
            User User = UserOptional.get();
            try {
                String uploadDir = "uploads/Users/";
                String fileName = id + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
                User.setUrlImage(fileName);
                userRepository.save(User);
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

    public User uploadProfilePicture(Long id, MultipartFile image) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
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
}