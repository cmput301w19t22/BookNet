package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Activity to edit a book's description.
 *
 * @author Jamie
 * @version 1.0
 */
public class EditBookActivity extends AppCompatActivity {

    //Layout Objects
    private EditText titleField;
    private EditText authorField;
    private EditText isbnField;
    private EditText descriptionField;
    private Button addButton;
    private Button cancelButton;
    private Button scanButton;

    private DatabaseManager manager = DatabaseManager.getInstance();

    //The book to edit
    private BookListing listing;
    private Book book = new Book();

    /**
     * Called when creating the activity.
     * Obtains the layout objects and sets listeners.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);//reuse the add book ui

        //Obtain Refs to Layout Objects
        titleField = findViewById(R.id.titleField);
        authorField = findViewById(R.id.authorField);
        isbnField = findViewById(R.id.isbnField);
        descriptionField = findViewById(R.id.descriptionField);

        addButton = findViewById(R.id.addButton);
        cancelButton = findViewById(R.id.cancelButton);
        scanButton = findViewById(R.id.scanButton);

        addButton.setText("Apply");//change the text to suit this activity

        //Get Intent
        Intent intent = getIntent();
        //Check if given info to fetch listing
        if (intent.hasExtra("bookisbn")) {
            String isbn = intent.getStringExtra("bookisbn");
            listing = manager.readUserOwnedBookListingWithISBN(isbn);
            book = listing.getBook();
        }

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
                editBookRequest(book);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNow();
            }
        });
        //#endregion
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
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setDescription(description);
    }

    /**
     * Creates a request to add the book to the user's library
     *
     * @param book The book to add
     */
    private void editBookRequest(Book book) {
        //CurrentUser.getInstance().requestAddBook(book);//todo call edit book to db from here
    }

    // ISBN Scanner

    /**
     * Handles the retrieval of the Data from the ISBN scanner
     *
     * @author Andi Aspin - https://www.youtube.com/watch?v=PRIVHoEyeL0&t=41s
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show();
            } else {
                isbnField.setText(result.getContents());
            }
        }
    }

    /**
     * Runs the code needed for the use of the ISBN scanner
     *
     * @author Andi Aspin - https://www.youtube.com/watch?v=PRIVHoEyeL0&t=41s
     */
    private void scanNow() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(Portrait.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Your Barcode");
        integrator.initiateScan();
    }
}
