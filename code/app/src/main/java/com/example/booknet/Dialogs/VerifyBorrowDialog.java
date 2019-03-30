package com.example.booknet.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.Constants.BookListingStatus;
import com.example.booknet.Model.BookListing;
import com.example.booknet.R;

/**
 * Activity for fulfilling a book exchange. Uses a scanner to verify the borrow took place.
 */
public class VerifyBorrowDialog extends ISBNScannerDialog {

    //Layout Objects
    private Button scanButton;
    private Button cancelButton;
    private TextView infoText;
    private TextView titleText;


    //Dialog Data
    private BookListing listing;
    private boolean isMyBook;

    /**
     * Creates a new instance of this dialog.
     *
     * @param listing  The listing to be verified
     * @param isMyBook Whether the book belongs to the current user.
     * @return The new NewBookDialog instance
     */
    public static VerifyBorrowDialog newInstance(BookListing listing, boolean isMyBook) {
        VerifyBorrowDialog fragment = new VerifyBorrowDialog();
        Bundle args = new Bundle();
        //args.putString("tag", val);
        //todo put extras for data
        fragment.listing = listing;
        fragment.isMyBook = isMyBook;
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when creating the dialog.
     * Obtains the layout objects and sets listeners.
     *
     * @param savedInstanceState
     */
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create the Dialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_verify_borrow, null);
        builder.setView(dialogView);

        //Setup Layout
        scanButton = dialogView.findViewById(R.id.scanButton);
        cancelButton = dialogView.findViewById(R.id.cancelButton);
        infoText = dialogView.findViewById(R.id.infoText);
        titleText = dialogView.findViewById(R.id.titleText);

        if (listing != null) {
            if (listing.getStatus() == BookListingStatus.Accepted) {
                //Verifying Borrow
                titleText.setText("Borrowing");
            } else if (listing.getStatus() == BookListingStatus.Borrowed) {
                //Verifying Return
                titleText.setText("Returning");
            }
        }


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNow();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    /**
     * @param isbn
     */
    @Override
    protected void onScanResults(String isbn) {
        if (ISBNScannerDialog.isValidISBNFormat(isbn)) {
            if (listing.getISBN().equals(isbn)) {
                Log.d("isbn", "scanned isbn matches, verification ok");
            } else {
                Log.d("isbn", "scanned isbn not match");
            }
            verifyTransaction();//todo move into matching if above
        } else {
            Toast.makeText(getContext(), "Wasn't a valid ISBN", Toast.LENGTH_LONG);
        }
    }

    /**
     * Sends a message to the database that the scan was verified.
     * Called after scanning and verifying the scanned ISBN matches the listing
     */
    private void verifyTransaction() {
        if (isMyBook) {
            listing.setVerifiedByOwner(true);
            if (listing.isVerifiedByBorrower()) {
                infoText.setText("Transaction Complete.");
            } else {
                infoText.setText("Verified.\nPlease have the borrower scan this book to complete.");
            }
        } else {
            listing.setVerifiedByBorrower(true);
            if (listing.isVerifiedByOwner()) {
                infoText.setText("Transaction Complete.");
            } else {
                infoText.setText("Verified.\nPlease have the owner scan this book to complete.");
            }
        }
        //todo need to send anything to db?

    }

}
