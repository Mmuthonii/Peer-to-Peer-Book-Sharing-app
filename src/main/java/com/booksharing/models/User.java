package com.booksharing.models;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String profile_picture;
    private String gender;
    private String dob;
    private String location;  // ✅ Ensure ONLY ONE declaration

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfile_picture() { return profile_picture; }
    public void setProfile_picture(String profile_picture) { this.profile_picture = profile_picture; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getLocation() { return location; }  // ✅ Only one getter
    public void setLocation(String location) { this.location = location; }  // ✅ Only one setter
}
