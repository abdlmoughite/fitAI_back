package com.hessati.hessati.controllers;

import com.hessati.hessati.dto.UserDTO;
import com.hessati.hessati.entities.User;
import com.hessati.hessati.requests.user.PasswordChangeRequest;
import com.hessati.hessati.services.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @PageableDefault(size = 10) Pageable pageable) {

        Page<User> users = userService.getAllUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<User> saveUser(@RequestBody UserDTO userDto) {

        User savedUser = userService.saveUser(userDto);

        if (savedUser != null)
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

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

        if (user.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

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


    @PostMapping(
            value = "/edit-profile-image/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<User> uploadProfilePicture(
            @PathVariable Long id,
            @RequestPart(value = "profilePicture", required = false) MultipartFile image) {

        try {

            User user = userService.uploadProfilePicture(id, image);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(user);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }



    @PostMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestBody PasswordChangeRequest request) {

        try {

            userService.changePassword(
                    id,
                    request.getCurrentPassword(),
                    request.getNewPassword()
            );

            return ResponseEntity.ok().build();


        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }



    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getUserStats(@PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    userService.getUserStats(id)
            );


        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }



    @GetMapping("/{id}/weekly-summary")
    public ResponseEntity<?> getWeeklySummary(@PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    userService.getWeeklySummary(id)
            );


        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }



    @GetMapping("/{id}/monthly-progress")
    public ResponseEntity<?> getMonthlyProgress(@PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    userService.getMonthlyProgress(id)
            );


        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }



    @PostMapping("/{id}/record-workout")
    public ResponseEntity<?> recordWorkout(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        try {

            int minutes =
                    ((Number) body.getOrDefault("minutes", 0))
                            .intValue();


            int calories =
                    ((Number) body.getOrDefault("calories", 0))
                            .intValue();


            userService.recordWorkout(
                    id,
                    minutes,
                    calories
            );


            return ResponseEntity.ok().build();


        } catch (RuntimeException e) {


            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}