package com.example.booknet;

import org.junit.Test;
import static org.junit.Assert.*;

public class BookTest {

    @Test
    public void Constructors() {

        String title = "Harry Potter and the Prisoner of Azkaban";
        String author = "J.K. Rowling";
        String description = "A magical boy goes down a toilet to fight a snake";
        String isbn = "0747542155";


        Book book = new Book(title, author, description, isbn);

        assertEquals(title,book.getTitle());
        assertEquals(author,book.getAuthor());
        assertEquals(description,book.getDescription());
        assertEquals(isbn, book.getIsbn());

        //Constructor 2
        book = new Book();

        assertEquals("",book.getTitle());
        assertEquals("",book.getAuthor());
        assertEquals("",book.getDescription());
        assertEquals("",book.getIsbn());

    }

    @Test
    public void Setters() {

        String title = "Harry Potter and the Prisoner of Azkaban";
        String author = "J.K. Rowling";
        String description = "A magical boy goes down a toilet to fight a snake";
        String isbn = "0747542155";

        Book book = new Book("init","init","init", "init");

        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setIsbn(isbn);

        assertEquals(title ,book.getTitle());
        assertEquals(author,book.getAuthor());
        assertEquals(description,book.getDescription());
        assertEquals(isbn,book.getIsbn());
    }
}
