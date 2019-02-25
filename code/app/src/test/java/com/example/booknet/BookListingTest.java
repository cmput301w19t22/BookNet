package com.example.booknet;

import android.location.Location;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookListingTest {


    @Test
    public void Constructor() {
        Book book = new Book("title", "author", "description");
        BookListing listing = new BookListing(book, "owner");

        assertEquals(book, listing.getBook());
        assertEquals("owner", listing.getOwnerName());
    }

    @Test
    public void AddRequest() {
        Book book = new Book("title", "author", "description");
        BookListing listing = new BookListing(book, "owner");

        listing.addRequest("requester1");

        ArrayList<String> requesters = listing.getRequesterNames();

        assertTrue("Request Added", requesters.contains("requester1"));

        assertEquals(BookListing.Status.Requested, listing.getStatus());
    }

    @Test
    public void AcceptRequest() {
        Book book = new Book("title", "author", "description");
        BookListing listing = new BookListing(book, "owner");

        listing.addRequest("requester1");
        listing.addRequest("requester2");

        listing.acceptRequest("requester1");

        assertTrue(listing.getRequesterNames().size()<=1);
        assertEquals("requester1", listing.getBorrowerName());

        assertEquals(BookListing.Status.Accepted, listing.getStatus());
    }

    @Test
    public void DenyRequest() {
        Book book = new Book("title", "author", "description");
        BookListing listing = new BookListing(book, "owner");

        listing.addRequest("requester1");
        listing.addRequest("requester2");

        listing.denyRequest("requester1");

        //Check removed from requesters
        ArrayList<String> requesters = listing.getRequesterNames();
        assertFalse("Request Denied", requesters.contains("requester1"));

        //Still one request left
        assertEquals(BookListing.Status.Requested, listing.getStatus());

        //Remove all requests
        listing.denyRequest("requester2");
        assertEquals(BookListing.Status.Available, listing.getStatus());
    }

    @Test
    public void BookBorrowed() {
        Book book = new Book("title", "author", "description");
        BookListing listing = new BookListing(book, "owner");

        listing.addRequest("requester1");
        listing.addRequest("requester2");

        listing.acceptRequest("requester1");
        listing.bookBorrowed();

        assertEquals("requester1", listing.getBorrowerName());
        assertTrue(listing.getRequesterNames().isEmpty());
        assertEquals(BookListing.Status.Borrowed, listing.getStatus());
    }

    @Test
    public void BookReturned() {
        Book book = new Book("title", "author", "description");
        BookListing listing = new BookListing(book, "owner");

        listing.addRequest("requester1");
        listing.addRequest("requester2");

        listing.acceptRequest("requester1");
        listing.bookBorrowed();
        listing.bookReturned();

        assertNotEquals("requester1", listing.getBorrowerName());
        assertEquals(BookListing.Status.Available, listing.getStatus());
    }

    @Test
    public void SetGeoLocation(){
        Book book = new Book("title", "author", "description");
        BookListing listing = new BookListing(book, "owner");

        //listing.setGeoLocation(new Location());

        assertNotNull(listing.getGeoLocation());
    }
}
