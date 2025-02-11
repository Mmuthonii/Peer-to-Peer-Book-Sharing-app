package com.booksharing.controllers;

import com.booksharing.models.Notification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private static final String FILE_PATH = "data/notifications.json";
    private static final Gson gson = new Gson();

    private List<Notification> loadNotifications() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Notification>>() {}.getType();
            List<Notification> notifications = gson.fromJson(reader, listType);
            return (notifications != null) ? new ArrayList<>(notifications) : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveNotifications(List<Notification> notifications) {
        try (Writer writer = new FileWriter(FILE_PATH, false)) {
            gson.toJson(notifications, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ 1️⃣ Fetch Notifications for a User
    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable String userId) {
        List<Notification> notifications = loadNotifications();
        List<Notification> userNotifications = notifications.stream()
                .filter(notification -> notification.getUserId().equals(userId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userNotifications);
    }

    // ✅ 2️⃣ Mark Notification as Read
    @PutMapping("/mark-read/{notificationId}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable String notificationId) {
        List<Notification> notifications = loadNotifications();
        boolean updated = false;

        for (Notification notification : notifications) {
            if (notification.getId().equals(notificationId)) {
                notification.setRead(true);
                updated = true;
                break;
            }
        }

        if (updated) {
            saveNotifications(notifications);
            return ResponseEntity.ok("✅ Notification marked as read.");
        } else {
            return ResponseEntity.badRequest().body("❌ Notification not found.");
        }
    }
}
