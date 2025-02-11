package com.booksharing.models;

public class Notification {
    private String id;
    private String requestId;
    private String userId;
    private String message;
    private boolean read;

    public Notification() {}

    // ✅ Correct Constructor - Ensure It Matches Usage in `RequestController.java`
    public Notification(String id, String requestId, String userId, String message, boolean read) {
        this.id = id;
        this.requestId = requestId;
        this.userId = userId;
        this.message = message;
        this.read = read;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
