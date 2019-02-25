package com.example.booknet;


import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class UserAccountTest {

    @Test
    public void Constructor() {
        UserAccount user = new UserAccount("user1", "password");

        assertEquals("user1", user.getUsername());
        assertEquals("password", user.getAccountPassword());
    }

    @Test
    public void Setters() {
        UserAccount user = new UserAccount("user1", "password");

        //Check Id and Password
        user.setUsername("newname");
        user.setAccountPassword("newpass");

        assertEquals("newname", user.getUsername());
        assertEquals("newpass", user.getAccountPassword());


        //Check BookLibrary
        BookLibrary library = new BookLibrary();
        user.setOwnedLibrary(library);
        assertEquals(library, user.getOwnedLibrary());

        //Check Requested Books
        BookLibrary requests = new BookLibrary();
        user.setRequestedBooks(requests);
        assertEquals(requests, user.getRequestedBooks());

        //Reviews
        ArrayList<Review> reviews = new ArrayList<Review>();
        reviews.add(new Review(user));
    }

}
