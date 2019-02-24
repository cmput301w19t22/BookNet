package com.example.booknet;

public class Book {
    private String title, author, description;

    // Constructors
    public void Book(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }
    public void Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.description = "";
    }
    public void Book(String title) {
        this.title = title;
        this.author = "";
        this.description = "";
    }
    public void Book() {
        this.title = "";
        this.author = "";
        this.description = "";
    }

    // Basic Setters
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // Basic Getters
    public String getTitle() {
        return this.title;
    }
    public String getAuthor() {
        return this.author;
    }
    public String getDescription() {
        return this.author;
    }
}
