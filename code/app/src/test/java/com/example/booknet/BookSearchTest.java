package com.example.booknet;

import com.example.booknet.Model.BookListing;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

public class BookSearchTest {

    @Test
    public void SearchListings() {
        ArrayList<String> keywords = new ArrayList<>();
        BookSearch search = new BookSearch();

        ArrayList<BookListing> results = search.searchListings(keywords);

        assertNotNull(results);
    }
}
