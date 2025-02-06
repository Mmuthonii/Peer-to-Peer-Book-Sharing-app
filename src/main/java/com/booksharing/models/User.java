package com.booksharing.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String username;
    private String email;
    private String dob;
    private String gender;
    private String city;
    private String password;

    public User(String username, String email, String dob, String gender, String city, String password) {
        this.username = username;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.city = city;
        this.password = password;
    }
}
