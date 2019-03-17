package com.example.booknet;

import java.util.ArrayList;

/**
 * A fake memory based database for testing the activities. Uses the same function
 * signatures as the real database but overrides their functions.
 *
 * @author Jamie
 * @see DatabaseManager
 */

@Deprecated
public class MockDatabase {

    //Data
//    ArrayList<BookListing> bookListings;
    BookLibrary library;
    ArrayList<UserAccount> userAccounts;
    ArrayList<Book> books;
    private static MockDatabase manager = new MockDatabase();

    private MockDatabase() {
    }

    static public MockDatabase getInstance() {
        return manager;
    }


//
//    public MockDatabase(BookLibrary lib){
//        library = lib;
//
//        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/UserOwned/"+CurrentUser.getInstance().getUserAccount().getUsername());
//        //bookListings = new ArrayList<>();
//
//        // Attach a listener to read the data at our posts reference
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    BookListing bookListing = data.child("BookListings").getValue(BookListing.class);
//                    if (bookListing != null) {
//                        System.out.println(bookListing.getOwnerUsername());
//                        library.addBookListing(bookListing);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });
//
//
//    }

    /**
     * Inserts testing data into the mock database
     */
    private void populateDatabase() {
//        UserAccount user1 = new UserAccount("User1", "debug");
//        UserAccount user2 = new UserAccount("User2", "debug");
//        UserAccount user3 = new UserAccount("User3", "debug");
//
//        Book book1 = new Book("Title1", "Author1", "Description", "1000");
//        Book book2 = new Book("Title2", "Author2", "Description", "1001");
//        Book book3 = new Book("Title3", "Author3", "Description", "1002");
//        Book book4 = new Book("Title4", "Author4", "Description", "1003");
//        Book book5 = new Book("Title5", "Author5", "Description", "1004");
//        Book book6 = new Book("Title6", "Author6", "Description", "1005");
//
//        user1.addBookToOwned(book1);
//        user2.addBookToOwned(book2);
//        user2.addBookToOwned(book3);
//        user3.addBookToOwned(book4);
//        user3.addBookToOwned(book5);
//        user3.addBookToOwned(book6);
//
//        writeUserAccount(user1);
//        writeUserAccount(user2);
//        writeUserAccount(user3);
//        writeUserAccount(CurrentUser.getInstance().getUserAccount());
    }


    public void writeUserAccount(UserAccount account) {
//        //Remove old version
//        for (UserAccount user : userAccounts) {
//            if (user.getUsername().equals(account.getUsername())) {
//                userAccounts.remove(user);
//            }
//        }
//        userAccounts.add(account);
//        //Write user's library as well
//        BookLibrary owned = account.getOwnedLibrary();
//        for (BookListing listing : owned.getBooks()) {
//            writeToAllBookListings(listing);
//        }
    }

//    @Override
//    public void writeToAllBookListings(UserAccount account, BookListing listing) {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//
//        //Get Info for this listing
//        String currentUserName = listing.getOwnerUsername();
//        Book currentBook = listing.getBook();
//
//        DatabaseReference userBookListingsRef = ref.child("UserOwned").child(currentUserName).child("BookListings");
//
//        userBookListingsRef.push().setValue(listing);
//    }

    public static void writeBookListing(UserAccount account, BookListing listing) {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//
//        String currentUserName = listing.getOwnerUsername();
//
//        DatabaseReference userBookListingsRef = ref.child("UserOwned").child(currentUserName).child("BookListings");
//
//        userBookListingsRef.push().setValue(listing);
    }


    public void writeBookInfo(Book book) {
//        books.add(book);
    }


    public void writeReview(Review review) {

    }


    public void removeBookListing(BookListing bookListing) {
//        CurrentUser.getInstance().getOwnedLibrary().removeBookListing(bookListing);
//        writeUserAccount(CurrentUser.getInstance().getUserAccount());
    }


    public UserAccount readUserAccount(String username) {
//        //Find the user in the array
//        for (UserAccount user : userAccounts) {
//            if (user.getUsername().equals(username)) {
//                return user;
//            }
//        }
        return null;
    }

    public Book readBookInfo(String isbn) {
//        return super.readBookInfo(isbn);
        return null;
    }


    public ArrayList<Review> readUserReviews(String username) {
//        return super.readUserReviews(username);
        return null;
    }


    //Constants for the below function
    public static final int OWNEDLIBRARY = 0;
    public static final int REQUESTLIBRARY = 1;

    /**
     * Reads a specific BookListing
     *
     * @param username Owner
     * @param bookisbn Book Id
     * @param library  Which library to search? Owned = 0, Requests = 1
     * @return
     */
    public BookListing readBookListing(String username, String bookisbn, int library) {
//        BookLibrary bookLibrary = null;
//        if (library == 0) {
//            bookLibrary = readUserOwnedLibrary(username);
//        } else if (library == 1) {
//            bookLibrary = readUserRequests(username);
//        }
//        if (bookLibrary != null) {
//            //Search library for the book
//            ArrayList<BookListing> listings = bookLibrary.getBooks();
//            for (BookListing bookListing : listings) {
//                if (bookListing.getBook().getIsbn().equals(bookisbn)) {
//                    return bookListing;
//                }
//            }
//        }

        return null;
    }


    public BookLibrary readUserOwnedLibrary(String username) {


//        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        ref.child("users").child(username)
//
//        //bookListings = new ArrayList<>();
//
//        // Attach a listener to read the data at our posts reference
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    BookListing bookListing = data.child("BookListing").getValue(BookListing.class);
//                    if (bookListing != null) {
//                        System.out.println(bookListing.getOwnerUsername());
//                        activity.addListingToList(bookListing);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });
//
//
//
//
//
//        //Find the user in the array
//
//        try {
//            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
//        }catch(Exception e){
//
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
//            String sStackTrace = sw.toString();
//
//            Log.d("mattTag", sStackTrace);
//        }
//
//
////        for (UserAccount user : userAccounts) {
////            if (user.getUsername().equals(username)) {
////                return user.getOwnedLibrary();
////            }
////        }
        return null;
    }


    public BookLibrary readUserRequests(String username) {
//        //Find the user in the array
//        for (UserAccount user : userAccounts) {
//            if (user.getUsername().equals(username)) {
//                return user.getRequestedBooks();
//            }
//        }
        return null;
    }
    //#endregion
}
