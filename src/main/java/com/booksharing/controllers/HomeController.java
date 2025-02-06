package com.booksharing.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/") // Base route
public class HomeController {

    @GetMapping // Handles requests to "/"
    public String home() {
        return "Welcome to the Book Sharing App!";
    }
}
