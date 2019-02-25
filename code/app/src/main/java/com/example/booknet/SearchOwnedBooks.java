package com.example.booknet;

import java.util.ArrayList;

public class SearchOwnedBooks {
    private ArrayList<String> keywords;

    public SearchOwnedBooks(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }
}
