package com.hessati.hessati.controllers;

import com.hessati.hessati.dto.UserDTO;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.requests.user.PasswordChangeRequest;
import com.hessati.hessati.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @PageableDefault(size = 10) Pageable pageable) { // default 10 per page
        Page<User> users = userService.getAllUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<User> saveUser(@RequestBody UserDTO userDto) {
        User savedUser = userService.saveUser(userDto);
        if(savedUser != null)
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserDTO user) {
        User updatedUser = userService.updateUser(user.getId(), user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        return ResponseEntity.ok(isDeleted);
    }

    @PostMapping(value = "/edit-profile-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> uploadProfilePicture(
            @PathVariable Long id,
            @RequestPart(value = "profilePicture", required = false) MultipartFile image) {
        User user = userService.uploadProfilePicture(id, image);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/change-password/{id}")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,
                                               @RequestBody PasswordChangeRequest request) {
        userService.changePassword(id, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}