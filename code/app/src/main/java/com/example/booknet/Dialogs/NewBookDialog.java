package com.example.booknet.Dialogs;

import android.app.Dialog;
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
import android.widget.Toast;

import com.example.booknet.Model.Book;
import com.example.booknet.Model.CurrentUser;
import com.example.booknet.R;

/**
 * Activity to create a new book. Can create the book by scanning
 * an ISBN or by entering the data manually.
 *
 * @author Jamie
 * @version 1.0
 */
public class NewBookDialog extends ISBNScannerDialog {

    //Layout Objects
    private EditText titleField;
    private EditText authorField;
    private EditText isbnField;
    private EditText descriptionField;
    private Button addButton;
    private ImageButton cancelButton;
    private ImageButton scanButton;


    /**
     * Creates a new instance of this dialog.
     *
     * @return The new NewBookDialog instance
     */
    public static NewBookDialog newInstance() {
        NewBookDialog fragment = new NewBookDialog();
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
        View dialogView = inflater.inflate(R.layout.activity_new_book, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        //Obtain Refs to Layout Objects
        titleField = dialogView.findViewById(R.id.titleField);
        authorField = dialogView.findViewById(R.id.authorField);
        isbnField = dialogView.findViewById(R.id.isbnField);
        descriptionField = dialogView.findViewById(R.id.descriptionField);

        addButton = dialogView.findViewById(R.id.addButton);
        cancelButton = dialogView.findViewById(R.id.cancelButton);
        scanButton = dialogView.findViewById(R.id.scanButton);


        //#region Set Listeners
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a book and add it to the user library
                Book book = createBook();
                addBookRequest(book);
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
        if (title.trim().length() == 0) {
            return false;
        }
        if (author.trim().length() == 0) {
            return false;
        }
        //todo any further validation?
        return true;
    }

    /**
     * Attempts to create a book with the data in the activity's fields.
     *
     * @return A new book from the data
     */
    private Book createBook() {
        //Get the data from the layout fields
        String title = titleField.getText().toString().trim();
        String author = authorField.getText().toString().trim();
        String isbn = isbnField.getText().toString();
        String description = descriptionField.getText().toString().trim();

        //Create and return the book
        Book newBook = new Book(title, author, description, isbn);
        return newBook;
    }

    /**
     * Creates a request to add the book to the user's library
     *
     * @param book The book to add
     */
    private void addBookRequest(Book book) {
        CurrentUser.getInstance().requestAddBook(book);
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
}
