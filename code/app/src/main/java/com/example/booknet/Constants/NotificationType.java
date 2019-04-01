package com.example.booknet.Constants;

public enum NotificationType {
    hasRequested,
    hasAccepted,
    hasDeclined;

    /**
     * Returns the InAppNotification type as a text string.
     *
     * @return
     */
    @Override
    public String toString() {
        switch (this) {
            case hasRequested:
                return " has requested a book";
            case hasAccepted:
                return " has accepted your book request";
            case hasDeclined:
                return " has declined your book request";
        }
        return super.toString();
    }
}
