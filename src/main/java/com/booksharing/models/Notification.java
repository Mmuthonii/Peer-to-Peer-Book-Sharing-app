package com.booksharing.models;

public class Notification {
    private String id;
    private String requestId;
    private String receiverId;
    private String message;
    private boolean read; // Read status

    // Constructor
    public Notification(String id, String requestId, String receiverId, String message, boolean read) {
        this.id = id;
        this.requestId = requestId;
        this.receiverId = receiverId;
        this.message = message;
        this.read = read;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
