package com.booksharing.controllers;

import com.booksharing.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;    // ✅ Fix missing import
import java.util.HashMap;

@RestController
@RequestMapping("/users") // ✅ This must match the API path
public class UserController {
    private static final String FILE_PATH = "data/users.json";
    private static final Gson gson = new Gson();

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User newUser) {
        List<User> users = loadUsers();

        // 🚀 Check if email already exists
        Optional<User> existingUser = users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(newUser.getEmail()))
                .findFirst();

        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("❌ Email already exists!");
        }

        // 🚀 Assign unique ID and add user
        newUser.setId(users.size() + 1);
        users.add(newUser);
        saveUsers(users);

        return ResponseEntity.ok("✅ User registered successfully!");
    }

    private void saveUsers(List<User> users) {
        try (Writer writer = new FileWriter(FILE_PATH, false)) {
            Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
            prettyGson.toJson(users, writer);
            System.out.println("✅ Users saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        List<User> users = loadUsers();

        Optional<User> foundUser = users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(loginUser.getEmail()) &&
                        user.getPassword().equals(loginUser.getPassword()))
                .findFirst();

        if (foundUser.isPresent()) {
            User user = foundUser.get();
            return ResponseEntity.ok(user);  // ✅ Return JSON object
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password."));
        }
    }

    private List<User> loadUsers() {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            Reader reader = new FileReader(FILE_PATH);
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(reader, listType);

            return (users != null) ? users : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return loadUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        List<User> users = loadUsers();
        Optional<User> user = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());  // ✅ Returns full user data
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found!");
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable int id) {
        List<User> users = loadUsers();
        boolean removed = users.removeIf(user -> user.getId() == id);
        if (removed) {
            saveUsers(users);
            return "✅ User deleted successfully!";
        } else {
            return "❌ User not found!";
        }
    }
}


