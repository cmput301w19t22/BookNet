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

public class NewBookActivity extends AppCompatActivity {

    //Layout Objects
    EditText titleField;
    EditText authorField;
    EditText isbnField;
    EditText descriptionField;
    private Button addButton;
    private Button cancelButton;
    private Button scanButton;

    //Data
    BookLibrary userLibrary;//temp for testing

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
        scanButton = findViewById(R.id.scanButton);

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
                //todo anything else here?
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
     * Attempts to create a book.
     */
    private Book createBook() {
        String title = titleField.getText().toString();
        String author = authorField.getText().toString();
        String isbn = isbnField.getText().toString();
        String description = descriptionField.getText().toString();

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

    // ISBN Scanner
    /**
     * Handles the retrieval of the Data from the ISBN scanner
     * @author Andi Aspin - https://www.youtube.com/watch?v=PRIVHoEyeL0&t=41s
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,"Result Not Found", Toast.LENGTH_SHORT).show();
            } else {
                isbnField.setText(result.getContents());
            }
        }
    }
    /**
     * Runs the code needed for the use of the ISBN scanner
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
