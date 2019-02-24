package com.example.booknet;

import java.util.ArrayList;

public class OwnedBooks {
    private ArrayList<Book> books;

    public OwnedBooks() {
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

    public void loanBook(Book book) {

    }

    public void RecieveBook(Book book) {}

}
