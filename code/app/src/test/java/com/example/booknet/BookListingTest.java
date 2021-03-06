package com.example.booknet;

import com.example.booknet.Constants.BookListingStatus;
import com.example.booknet.Model.Book;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.UserAccount;

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
        Book book = new Book("title", "author", "description", "1234567890");
        BookListing listing = new BookListing(book);
        UserAccount owner = CurrentUser.getInstance().getUserAccount();

        assertEquals(book, listing.getBook());
        assertEquals(owner.getUsername(), listing.getOwnerUsername());
    }

    @Test
    public void AddRequest() {
        Book book = new Book("title", "author", "description", "1234567890");
        UserAccount owner = new UserAccount("test_owner");
        BookListing listing = new BookListing(book);

        listing.addRequest("requester1");

        ArrayList<String> requestersList = listing.getRequests();

        assertTrue("Request Added", requestersList.contains("requester1"));

        assertEquals(BookListingStatus.Requested, listing.getStatus());
    }

    @Test
    public void AcceptRequest() {
        Book book = new Book("title", "author", "description", "1234567890");
        UserAccount owner = new UserAccount("test_owner");
        BookListing listing = new BookListing(book);

        listing.addRequest("requester1");
        listing.addRequest("requester2");

        listing.acceptRequest("requester1");

        assertTrue(listing.getRequests().size()==0);
        assertEquals("requester1", listing.getBorrowerName());

        assertEquals(BookListingStatus.Accepted, listing.getStatus());
    }

    @Test
    public void DenyRequest() {
        Book book = new Book("title", "author", "description", "1234567890");
        UserAccount owner = new UserAccount("test_owner");
        BookListing listing = new BookListing(book);

        listing.addRequest("requester1");
        listing.addRequest("requester2");

        listing.denyRequest("requester1");

        //Check removed from requesters
        ArrayList<String> requesters = listing.getRequests();
        assertFalse("Request Denied", requesters.contains("requester1"));

        //Still one request left
        assertEquals(BookListingStatus.Requested, listing.getStatus());
        assertTrue(requesters.contains("requester2"));

        //Remove all requests
        listing.denyRequest("requester2");
        assertEquals(BookListingStatus.Available, listing.getStatus());
    }

    @Test
    public void BookBorrowed() {
        Book book = new Book("title", "author", "description", "1234567890");
        UserAccount owner = new UserAccount("test_owner");
        BookListing listing = new BookListing(book);

        listing.addRequest("requester1");
        listing.addRequest("requester2");

        listing.acceptRequest("requester1");
        listing.bookBorrowed();

        assertEquals("requester1", listing.getBorrowerName());
        assertTrue(listing.getRequests().isEmpty());
        assertEquals(BookListingStatus.Borrowed, listing.getStatus());
    }

    @Test
    public void BookReturned() {
        Book book = new Book("title", "author", "description", "1234567890");
        UserAccount owner = new UserAccount("test_owner");
        BookListing listing = new BookListing(book);

        listing.addRequest("requester1");
        listing.addRequest("requester2");

        listing.acceptRequest("requester1");
        listing.bookBorrowed();
        listing.bookReturned();

        assertNotEquals("requester1", listing.getBorrowerName());
        assertEquals(BookListingStatus.Available, listing.getStatus());
    }

    @Test
    public void SetGeoLocation(){
        Book book = new Book("title", "author", "description", "1234567890");
        UserAccount owner = new UserAccount("test_owner");
        BookListing listing = new BookListing(book);

        //listing.setGeoLocation(new Location());
        //todo: how to get location?

        assertNotNull(listing.getGeoLocation());
    }
}
