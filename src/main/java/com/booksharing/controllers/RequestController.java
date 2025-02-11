package com.booksharing.controllers;

import com.booksharing.models.Request;
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
@RequestMapping("/requests")
public class RequestController {
    private static final String REQUESTS_FILE = "data/requests.json"; // ✅ Store requests in JSON
    private static final String NOTIFICATIONS_FILE = "data/notifications.json"; // ✅ Store notifications in JSON
    private static final Gson gson = new Gson();

    // ✅ Load Requests from JSON
    private List<Request> loadRequests() {
        File file = new File(REQUESTS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(REQUESTS_FILE)) {
            Type listType = new TypeToken<List<Request>>() {}.getType();
            List<Request> requests = gson.fromJson(reader, listType);
            return (requests != null) ? requests : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // ✅ Save Requests to JSON
    private void saveRequests(List<Request> requests) {
        try (Writer writer = new FileWriter(REQUESTS_FILE, false)) {
            gson.toJson(requests, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    // ✅ 1️⃣ Send a Request & Notify Owner
    @PostMapping("/send")
    public ResponseEntity<String> sendRequest(@RequestBody Request request) {
        String requestId = UUID.randomUUID().toString();
        request.setId(requestId);
        request.setStatus("Pending");

        System.out.println("📌 Creating Request:");
        System.out.println("🔹 ID: " + requestId);
        System.out.println("🔹 Book: " + request.getBookTitle());
        System.out.println("🔹 Sender ID: " + request.getSenderId());
        System.out.println("🔹 Receiver ID: " + request.getReceiverId());

        List<Request> requests = loadRequests();
        requests.add(request);
        saveRequests(requests); // ✅ Save to JSON

        List<Notification> notifications = loadNotifications();
        Notification notification = new Notification(
                UUID.randomUUID().toString(),
                requestId,
                request.getReceiverId(),
                "📌 New request for: " + request.getBookTitle() + " from " + request.getSenderName(),
                false
        );
        notifications.add(notification);
        saveNotifications(notifications);

        System.out.println("✅ Request Sent Successfully!");
        return ResponseEntity.ok("✅ Request sent successfully!");
    }

    // ✅ 2️⃣ Get Requests Sent by User
    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<Request>> getSentRequests(@PathVariable String userId) {
        try {
            System.out.println("🔍 Fetching sent requests for user ID: " + userId);

            List<Request> requests = loadRequests();
            if (requests.isEmpty()) {
                System.out.println("⚠️ No requests found in requests.json.");
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<Request> userRequests = requests.stream()
                    .filter(request -> request.getSenderId() != null && request.getSenderId().equals(userId))
                    .collect(Collectors.toList());

            System.out.println("✅ Sent Requests for User " + userId + ": " + userRequests.size());
            return ResponseEntity.ok(userRequests);
        } catch (Exception e) {
            System.err.println("❌ Error fetching sent requests: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // ✅ 3️⃣ Get Requests Received by Owner
    @GetMapping("/received/{userId}")
    public ResponseEntity<List<Request>> getReceivedRequests(@PathVariable String userId) {
        try {
            System.out.println("🔍 Fetching received requests for user ID: " + userId);

            List<Request> requests = loadRequests();
            if (requests.isEmpty()) {
                System.out.println("⚠️ No requests found in requests.json.");
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<Request> incomingRequests = requests.stream()
                    .filter(request -> request.getReceiverId() != null && request.getReceiverId().equals(userId))
                    .collect(Collectors.toList());

            System.out.println("✅ Received Requests for User " + userId + ": " + incomingRequests.size());
            return ResponseEntity.ok(incomingRequests);
        } catch (Exception e) {
            System.err.println("❌ Error fetching received requests: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // ✅ 4️⃣ Accept or Reject a Request
    @PutMapping("/respond/{requestId}")
    public ResponseEntity<String> respondToRequest(@PathVariable String requestId, @RequestBody Map<String, String> response) {
        List<Request> requests = loadRequests();
        List<Notification> notifications = loadNotifications();

        boolean updated = false;
        String status = response.get("status");

        for (Request request : requests) {
            if (request.getId().equals(requestId)) {
                request.setStatus(status);
                updated = true;

                notifications.add(new Notification(
                        UUID.randomUUID().toString(),
                        requestId,
                        request.getSenderId(),
                        "📌 Your request for '" + request.getBookTitle() + "' has been " + status,
                        false
                ));
                break;
            }
        }

        if (updated) {
            saveRequests(requests);
            saveNotifications(notifications);
            return ResponseEntity.ok("✅ Request " + status);
        } else {
            return ResponseEntity.badRequest().body("❌ Request not found.");
        }
    }
}
