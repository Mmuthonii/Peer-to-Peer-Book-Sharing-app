package com.booksharing.models;

public class Book {
    private String id;
    private String title;
    private String author;
    private String genre;
    private String ownerId;
    private String image;
    private String condition;

    // ✅ Constructor (Default)
    public Book() {}

    // ✅ Constructor (Parameterized)
    public Book(String id, String title, String author, String genre, String ownerId, String image, String condition) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.ownerId = ownerId;
        this.image = image;
        this.condition = condition;
    }

    // ✅ Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
}
