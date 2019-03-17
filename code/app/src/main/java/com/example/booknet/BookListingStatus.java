package com.example.booknet;

/**
 * Enum for the status of a BookListing, so the values are more easily tracked
 */
public enum BookListingStatus {
    Available,
    Requested,
    Accepted,
    Borrowed;

    /**
     * Returns the status as a text string.
     *
     * @return
     */
    @Override
    public String toString() {
        switch (this) {
            case Available:
                return "Available";
            case Requested:
                return "Requested";
            case Accepted:
                return "Accepted";
            case Borrowed:
                return "Borrowed";
        }
        return super.toString();
    }
}
