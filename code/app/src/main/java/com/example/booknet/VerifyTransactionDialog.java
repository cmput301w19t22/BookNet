package com.example.booknet;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

/**
 * Activity for fulfilling a book exchange. Uses a scanner to verify the borrow took place.
 */
public class VerifyTransactionDialog extends ISBNScannerDialog {


    /**
     * Creates a new instance of this dialog.
     *
     * @return The new NewBookDialog instance
     */
    public static VerifyTransactionDialog newInstance() {
        VerifyTransactionDialog fragment = new VerifyTransactionDialog();
        Bundle args = new Bundle();
        //args.putString("tag", val);
        //todo put extras for data
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
        View dialogView = inflater.inflate(R.layout.activity_borrow_book, null);
        builder.setView(dialogView);

        //todo make layout

        return builder.create();
    }

    /**
     * @param isbn
     */
    @Override
    protected void onScanResults(String isbn) {
        if (ISBNScannerDialog.isValidISBNFormat(isbn)) {
            //isbnField.setText(isbn);

        } else {
            Toast.makeText(getContext(), "Wasn't a valid ISBN", Toast.LENGTH_LONG);
        }
    }

    /**
     * Sends a message to the database that the scan was verified.
     * Called after
     */
    private void verifyTransaction(){

    }
}
