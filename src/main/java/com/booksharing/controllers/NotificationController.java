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
    private static final String NOTIFICATIONS_FILE = "data/notifications.json";
    private static final Gson gson = new Gson();

    // ✅ Load Notifications from JSON
    private List<Notification> loadNotifications() {
        File file = new File(NOTIFICATIONS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(NOTIFICATIONS_FILE)) {
            Type listType = new TypeToken<List<Notification>>() {}.getType();
            List<Notification> notifications = gson.fromJson(reader, listType);
            return (notifications != null) ? notifications : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ✅ Save Notifications to JSON
    private void saveNotifications(List<Notification> notifications) {
        try (Writer writer = new FileWriter(NOTIFICATIONS_FILE, false)) {
            gson.toJson(notifications, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ 1️⃣ Fetch Notifications for a User
    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable String userId) {
        List<Notification> notifications = loadNotifications();
        List<Notification> userNotifications = notifications.stream()
                .filter(notification -> notification.getReceiverId().equals(userId) && !notification.isRead())
                .collect(Collectors.toList());

        return ResponseEntity.ok(userNotifications);
    }

    // ✅ 2️⃣ Mark Notifications as Read
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
