package com.example.booknet;

import org.junit.Test;
import static org.junit.Assert.*;

public class BookTest {

    @Test
    public void Constructors() {
        //Constructor 1
        Book book = new Book("Title","Author","Description", "ISBN");

        assertEquals("Title",book.getTitle());
        assertEquals("Author",book.getAuthor());
        assertEquals("Description",book.getDescription());
        assertEquals("ISBN", book.getDescription());

        //Constructor 2
        book = new Book();

        assertEquals("",book.getTitle());
        assertEquals("",book.getAuthor());
        assertEquals("",book.getDescription());
        assertNull(book.getIsbn());

    }

    @Test
    public void Setters() {
        Book book = new Book("init","init","init", "init");

        book.setTitle("New1");
        book.setAuthor("New2");
        book.setDescription("New3");
        book.setIsbn("New4");

        assertEquals("New1",book.getTitle());
        assertEquals("New2",book.getAuthor());
        assertEquals("New3",book.getDescription());
        assertEquals("New4",book.getIsbn());
    }
}
