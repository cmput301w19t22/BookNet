package com.example.booknet.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.Constants.BookListingStatus;
import com.example.booknet.DatabaseManager;
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

    private DatabaseManager manager = DatabaseManager.getInstance();

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
                if (isMyBook) {
                    titleText.setText("Lending");
                } else {
                    titleText.setText("Borrowing");
                }
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
                verifyTransaction();
            } else {
                Log.d("isbn", "scanned isbn not match");
            }
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
            BookListingStatus status = listing.getStatus();
            boolean complete = manager.verifyRequest(listing, isMyBook);
            if (complete) {
                titleText.setText("Transaction Complete");
                if (status == BookListingStatus.Accepted) {
                    infoText.setText("You may now hand over the book.");
                } else {
                    infoText.setText("You may receive your book.");
                }
            } else {
                titleText.setText("Verified");
                infoText.setText("Please have the borrower scan this book to complete.");
            }
        } else {
            listing.setVerifiedByBorrower(true);
            BookListingStatus status = listing.getStatus();
            boolean complete = manager.verifyRequest(listing, isMyBook);
            if (complete) {
                titleText.setText("Transaction Complete");
                if (status == BookListingStatus.Accepted) {
                    infoText.setText("Enjoy your book.");
                } else {
                    infoText.setText("You may return the book to the owner.");
                    try {
                        manager.cancelRequestForListing(listing);
                    } catch (DatabaseManager.DatabaseException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                titleText.setText("Verified");
                infoText.setText("Please have the owner scan this book to complete.");
            }
        }
        //ScaleAnimation anim = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
        //        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        TranslateAnimation anim = new TranslateAnimation(0f, 0f, 0f, 4f);
        anim.setInterpolator(new CycleInterpolator(3));
        anim.setDuration(2000);
        titleText.startAnimation(anim);
    }

    /**
     * Called when the dialog is dismissed. Tells the source activity the dialog dismissed
     * if it has the DialogCloseListener interface.
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Tell the source activity the dialog closed so it can update
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).onDialogClose();
        }
    }
}
