package com.example.booknet;

import java.util.ArrayList;

public class RequestedBooks {
    private ArrayList<Book> books;

    public RequestedBooks() {
        this.books = new ArrayList<Book>();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public void removeBook(Book book) {
        this.books.remove(book);
    }

    public void returnBook(Book book) {

    }

    public void borrowBook(Book book) {

    }
}
