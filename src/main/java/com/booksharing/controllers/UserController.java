package com.booksharing.controllers;

import com.booksharing.models.User;
import com.booksharing.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*") // Allows frontend to connect
public class UserController {
    private final UserService userService = new UserService();

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        boolean isRegistered = userService.registerUser(user);
        return isRegistered
                ? ResponseEntity.ok("User registered successfully!")
                : ResponseEntity.badRequest().body("Email already exists!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        boolean isAuthenticated = userService.authenticate(user.getEmail(), user.getPassword());
        return isAuthenticated
                ? ResponseEntity.ok("Login Successful")
                : ResponseEntity.badRequest().body("Invalid Credentials");
    }
}
