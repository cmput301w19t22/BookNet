package com.example.booknet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity to create a new book. Can create the book by scanning
 * an ISBN or by entering the data manually.
 *
 * @author Jamie
 * @version 1.0
 */
public class NewBookActivity extends AppCompatActivity {

    //Layout Objects
    EditText titleField;
    EditText authorField;
    EditText isbnField;
    EditText descriptionField;
    private Button addButton;
    private Button cancelButton;

    /**
     * Called when creating the activity.
     * Obtains the layout objects and sets listeners.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);


        //Obtain Refs to Layout Objects
        titleField = findViewById(R.id.titleField);
        authorField = findViewById(R.id.authorField);
        isbnField = findViewById(R.id.isbnField);
        descriptionField = findViewById(R.id.descriptionField);

        addButton = findViewById(R.id.addButton);
        cancelButton = findViewById(R.id.cancelButton);

        //#region Set Listeners
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a book and add it to the user library
                Book book = createBook();
                addBookRequest(book);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //#endregion
    }


    /**
     * Attempts to create a book with the data in the activity's fields.
     *
     * @return A new book from the data
     */
    private Book createBook() {
        //Get the data from the layout fields
        String title = titleField.getText().toString();
        String author = authorField.getText().toString();
        String isbn = isbnField.getText().toString();
        String description = descriptionField.getText().toString();

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
}
