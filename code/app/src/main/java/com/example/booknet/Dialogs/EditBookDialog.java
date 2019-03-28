package com.example.booknet.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.Book;
import com.example.booknet.Model.BookListing;
import com.example.booknet.R;

/**
 * Activity to edit a book's description.
 *
 * @author Jamie
 * @version 1.0
 */
public class EditBookDialog extends ISBNScannerDialog {

    //Layout Objects
    private TextView pageTitle;
    private EditText titleField;
    private EditText authorField;
    private EditText isbnField;
    private EditText descriptionField;
    private Button addButton;
    private Button cancelButton;
    private ImageButton scanButton;

    private DatabaseManager manager = DatabaseManager.getInstance();

    //The book to edit
    private BookListing listing;
    private Book book = new Book();

    /**
     * Creates a new instance of this dialog.
     *
     * @return The new NewBookDialog instance
     */
    public static EditBookDialog newInstance(BookListing item) {
        EditBookDialog fragment = new EditBookDialog();
        Bundle args = new Bundle();
        //args.putString("tag", val);
        fragment.listing = item;
        //todo put extras for data
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * Called when creating the activity.
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
        View dialogView = inflater.inflate(R.layout.activity_new_book, null);
        builder.setView(dialogView);


        //Obtain Refs to Layout Objects
        pageTitle = dialogView.findViewById(R.id.pageLabel);
        titleField = dialogView.findViewById(R.id.titleField);
        authorField = dialogView.findViewById(R.id.authorField);
        isbnField = dialogView.findViewById(R.id.isbnField);
        descriptionField = dialogView.findViewById(R.id.descriptionField);

        addButton = dialogView.findViewById(R.id.addButton);
        cancelButton = dialogView.findViewById(R.id.cancelButton);
        scanButton = dialogView.findViewById(R.id.scanButton);

        addButton.setText("Apply");//change the text to suit this activity
        pageTitle.setText("Edit Book");

        //Get Intent
        /*Intent intent = getIntent();
        //Check if given info to fetch listing
        if (intent.hasExtra("isbn")) {
            String isbn = intent.getStringExtra("isbn");
            int dupInt = intent.getIntExtra("dupInd", 0);
            listing = manager.readUserOwnedBookListing(isbn, dupInt);
            book = listing.getBook();
        }*/

        book = listing.getBook();


        //Fill Fields
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
        descriptionField.setText(book.getDescription());

        //#region Set Listeners
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a book and add it to the user library
                editBook();
                editBookRequest();
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNow();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validateFields()) {
                    addButton.setEnabled(true);
                } else {
                    addButton.setEnabled(false);
                }
            }
        };

        titleField.addTextChangedListener(textWatcher);
        authorField.addTextChangedListener(textWatcher);
        isbnField.addTextChangedListener(textWatcher);
        //#endregion

        return builder.create();
    }

    /**
     * Checks whether the input fields have valid data.
     *
     * @return True if all fields are valid
     */
    private boolean validateFields() {
        String title = titleField.getText().toString();
        String author = authorField.getText().toString();
        String isbn = isbnField.getText().toString();
        //String description = descriptionField.getText().toString();

        if (!ISBNScannerDialog.isValidISBNFormat(isbn)) {
            return false;
        }
        if (title.length() == 0) {
            return false;
        }
        if (author.length() == 0) {
            return false;
        }
        //todo any further validation?
        return true;
    }


    /**
     * Attempts to edit the book with the data in the activity's fields.
     */
    private void editBook() {
        //Get the data from the layout fields
        String title = titleField.getText().toString();
        String author = authorField.getText().toString();
        String isbn = isbnField.getText().toString();
        String description = descriptionField.getText().toString();

        //Modify the book
        listing.editTitle(title);
        listing.editAuthor(author);
        listing.editIsbn(isbn);
        listing.editDescription(description);

    }

    /**
     * Creates a request to write the edited book to the user's library in the database
     */
    private void editBookRequest() {
        manager.writeUserBookListing(listing);
    }

    /**
     * Action to take when the scan results are received.
     * Puts the scanned ISBN into the ISBN field.
     *
     * @param isbn The isbn that was scanned
     */
    @Override
    protected void onScanResults(String isbn) {
        if (ISBNScannerDialog.isValidISBNFormat(isbn)) {
            isbnField.setText(isbn);
        } else {
            Toast.makeText(getContext(), "Wasn't a valid ISBN", Toast.LENGTH_LONG);
        }
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
