package com.example.booknet.Model;

import com.example.booknet.Constants.BookListingStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Container for a list of book listings. Used for either a user's owned books, or
 * the books they have requested.
 */
public class BookLibrary implements Serializable, Iterable<BookListing> {


    private ArrayList<BookListing> books;

    /**
     * Constructor
     */
    public BookLibrary() {
        this.books = new ArrayList<BookListing>();
    }

    /**
     * Gets the list of BookListings.
     *
     * @return ArrayList containing BookListings
     */
    public ArrayList<BookListing> getBooks() {
        return books;
    }

    /**
     * Adds a BookListing to the library.
     *
     * @param bookListing The BookListing to add
     */
    public void addBookListing(BookListing bookListing) {
        this.books.add(bookListing);
    }

    /**
     * Removes a BookListing from the library.
     *
     * @param bookListing The BookListing to remove
     */
    public void removeBookListing(BookListing bookListing) {
        if (this.books.contains(bookListing)) {
            this.books.remove(bookListing);
        }
    }

    public void removeAllBooks(){
        books.clear();
    }

    /**
     * Get the number of books in this library.
     *
     * @return Size of the BookListing list
     */
    public int size() {
        return books.size();
    }

    /**
     * Checks whether a listing exists for a certain book in this library.
     *
     * @param book The book to check for
     * @return True if a listing is found for the book, false otherwise
     */
    public boolean containsListingFor(Book book) {
        for (BookListing listing : books) {
            if (listing.getBook().equals(book)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes a copy of this BookLibrary.
     *
     * @return A copied BookLibrary
     */
    public BookLibrary clone() {
        BookLibrary cloned = new BookLibrary();
        cloned.books = new ArrayList<>();
        for (BookListing booklisting: books) cloned.books.add(booklisting.clone());
        return cloned;
    }

    public ArrayList<BookListing> asArray(){
        return books;
    }

    @Override
    public Iterator<BookListing> iterator() {
        return books.iterator();
    }



    public BookListing getBookAtPosition(int position) {
        return books.get(position);
    }


    public void filterByStatus(BookLibrary library, BookListingStatus status) {
        books.clear();
        for (BookListing bookListing: library){

            if (bookListing.getStatus() == status){
                books.add(bookListing);
            }
        }
    }

    public void copyOneByOne(BookLibrary library) {
        removeAllBooks();
        for (BookListing bookListing: library){
            books.add(bookListing.clone());
        }
    }


    @Override
    public String toString() {
        String s = "";
        for (BookListing bookListing: books){
            s += bookListing.toString();
        }
        return s;
    }
}
