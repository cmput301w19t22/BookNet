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

    public void removeAllBooks() {
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

    public BookListing getListingMatching(BookListing listing) {
        for (BookListing b : this) {
            if (listing.isSameListing(b)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Makes a copy of this BookLibrary.
     *
     * @return A copied BookLibrary
     */
    public BookLibrary clone() {
        BookLibrary cloned = new BookLibrary();
        cloned.books = new ArrayList<>();
        for (BookListing booklisting : books) cloned.books.add(booklisting.clone());
        return cloned;
    }

    public int indexOf(BookListing listing) {
        return books.indexOf(listing);
    }

    public ArrayList<BookListing> asArray() {
        return books;
    }

    @Override
    public Iterator<BookListing> iterator() {
        return books.iterator();
    }


    public BookListing getBookAtPosition(int position) {
        return books.get(position);
    }

    /**
     * Fills this BookLibrary with the filtered results of the given BookLibrary
     *
     * @param library The library to filter and copy
     * @param status  The status of listing which will be in the filtered library.
     */
    public void filterByStatus(BookLibrary library, BookListingStatus status) {
        books.clear();
        for (BookListing bookListing : library) {

            if (bookListing.getStatus() == status) {
                books.add(bookListing);
            }
        }
    }

    /**
     * Fills this BookLibrary with the filtered results of the given BookLibrary.
     * Results are listings that contain any of the given statuses.
     *
     * @param library  The library to filter and copy
     * @param statuses The array of statuses to filter with
     */
    public void filterByStatus(BookLibrary library, ArrayList<BookListingStatus> statuses) {
        books.clear();
        for (BookListing bookListing : library) {
            if (statuses.contains(bookListing.getStatus())) {
                books.add(bookListing);
            }
        }
    }

    /**
     * Copies all the books from a given library into this one.
     *
     * @param library The library to copy from.
     */
    public void copyOneByOne(BookLibrary library) {
        removeAllBooks();
        for (BookListing bookListing : library) {
            books.add(bookListing.clone());
        }
    }


    @Override
    public String toString() {
        String s = "";
        for (BookListing bookListing : books) {
            s += bookListing.toString();
        }
        return s;
    }
}
