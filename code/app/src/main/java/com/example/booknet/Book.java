package com.example.booknet;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String description;
    private String isbn;

    // Constructors
    public Book(String title, String author, String description, String isbn) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
    }

    public Book() {
        this.title = "";
        this.author = "";
        this.description = "";
        this.isbn = "";
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

    public String getIsbn() {
        return isbn;
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

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    //#endregion
}
