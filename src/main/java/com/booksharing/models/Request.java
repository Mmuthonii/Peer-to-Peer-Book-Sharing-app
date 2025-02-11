package com.booksharing.models;

public class Request {
    private String id;
    private String bookTitle;
    private String bookImage;
    private String author;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String startDate;
    private String endDate;
    private String status;

    public Request() {}

    public Request(String id, String bookTitle, String bookImage, String author, String senderId, String senderName, String receiverId, String startDate, String endDate, String status) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.bookImage = bookImage;
        this.author = author;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookImage() { return bookImage; }
    public void setBookImage(String bookImage) { this.bookImage = bookImage; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

