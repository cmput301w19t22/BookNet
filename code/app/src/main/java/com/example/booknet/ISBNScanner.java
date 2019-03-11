package com.example.booknet;

/**
 * Class for scanning and verifying ISBN numbers.
 *
 * @author Jamie
 * @version 1.0
 */
public class ISBNScanner {

    //todo place actual scanning code here

    /**
     * Checks if the given string value is a valid ISBN format.
     * Does not check that the ISBN matches an existing book.
     *
     * @param value Value to validate
     * @return True if a valid format, False if not.
     */
    public final boolean isValidISBNFormat(String value) {
        //todo implement correct method, part 5 task
        //temp fake check
        if (value.length() <= 13) {
            return true;
        } else {
            return false;
        }
    }
}
