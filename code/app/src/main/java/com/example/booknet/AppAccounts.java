package com.example.booknet;

import java.util.ArrayList;

public class AppAccounts {
    private ArrayList<UserAccount> accounts;
    private SearchOwnedBooks search;

    public AppAccounts(ArrayList<UserAccount> accounts) {
        this.accounts = accounts;
    }

    public ArrayList<UserAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<UserAccount> accounts) {
        this.accounts = accounts;
    }

    public SearchOwnedBooks getSearch() {
        return search;
    }

    public void setSearch(SearchOwnedBooks search) {
        this.search = search;
    }
}
