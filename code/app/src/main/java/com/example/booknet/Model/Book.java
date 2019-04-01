package com.example.booknet.Model;

import java.io.Serializable;

/**
 * Simple data structure for a book.
 */
public class Book implements Serializable {
    //Attributes
    private String title;
    private String author;
    private String description;
    private String isbn;

    //#region Constructors

    /**
     * Constructor that takes all attributes as parameters.
     *
     * @param title       The title of the book
     * @param author      The author of the book
     * @param description The description of the book
     * @param isbn        The book's ISBN
     */
    public Book(String title, String author, String description, String isbn) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
    }

    /**
     * Constructor that creates a blank book object, with empty Strings for all the values.
     */
    public Book() {
        this.title = "";
        this.author = "";
        this.description = "";
        this.isbn = "";
    }
    //#endregion

    public boolean isBookISBNtheSame(Book book){
        return isbn.equals(book.isbn);


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

    public String toString(){
        return "book titled: " + title;
    }


}
