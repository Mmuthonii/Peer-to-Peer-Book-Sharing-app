package com.booksharing.services;

import com.booksharing.models.User;
import com.booksharing.utils.JsonStorage;
import com.google.gson.reflect.TypeToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class UserService {
    private final JsonStorage<User> jsonStorage;
    private final Type userListType = new TypeToken<List<User>>() {}.getType();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(JsonStorage<User> jsonStorage) {
        this.jsonStorage = jsonStorage;
    }

    public List<User> getUsers() {
        return jsonStorage.loadData(userListType);
    }

    public boolean emailExists(String email) {
        return getUsers().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public boolean registerUser(User user) {
        if (emailExists(user.getEmail())) {
            return false; // Email already exists
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password
        List<User> users = getUsers();
        users.add(user);
        jsonStorage.saveData(users);
        return true;
    }

    public boolean authenticate(String email, String rawPassword) {
        return getUsers().stream()
                .filter(user -> user.getEmail().equals(email))
                .anyMatch(user -> passwordEncoder.matches(rawPassword, user.getPassword())); // Compare hashed password
    }
}
