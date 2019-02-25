package com.example.booknet;

import java.util.ArrayList;

/**
 * Class that interfaces with the database
 */
public class DatabaseManager {

    //todo: attributes, if any

    //Constructor
    public DatabaseManager(){};


    //#region Methods
    //#region Writing To Database
    public void writeUserAccount(UserAccount account){
        //todo: implement
    }

    public void writeBookListing(BookListing listing){
        //todo: implement
    }

    public void writeBookInfo(Book book){
        //todo: implement
    }

    public void writeReview(Review review){
        //todo: implement
    }



    //#endregion

    //#region Reading From Database
    public UserAccount readUserAccount(String username){
        //todo: implement
        return null;
    }

    public Book readBookInfo(String isbn){
        //todo: implement
        return null;
    }

    public ArrayList<Review> readUserReviews(String username){
        //todo: implement
        return null;
    }

    public BookLibrary readUserOwnedLibrary(String username){
        //todo: implement
        return null;
    }

    public BookLibrary readUserRequests(String username){
        //todo: implement
        return null;
    }
    //#endregion
    //#endregion
}
