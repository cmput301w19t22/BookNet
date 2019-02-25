package com.example.booknet;

public class Book {
    private String title;
    private String author;
    private String description;

    // Constructors
    public Book(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public Book() {
        this.title = "";
        this.author = "";
        this.description = "";
    }

    //#region Getters Setters
    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getDescription() {
        return this.description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //#endregion
}
