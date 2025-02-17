package com.booksharing.models;

public class Book {
    private String id;
    private String title;
    private String author;
    private String genre;
    private String condition;
    private String ownerId;
    private String description;
    private String image;
    private String status;
    private String borrower;  // ✅ Add borrower field

    // ✅ Default Constructor
    public Book() {}

    // ✅ Constructor for Adding a New Book
    public Book(String id, String title, String author, String genre, String condition, String description, String image, String ownerId, String status, String borrower) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.condition = condition;
        this.description = description;
        this.image = image;
        this.ownerId = ownerId;
        this.status = status != null ? status : "Available";
        this.borrower = borrower;
    }

    // ✅ Getter and Setter for Borrower
    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    // ✅ Existing Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
