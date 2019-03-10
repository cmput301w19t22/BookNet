package com.example.booknet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OwnListingViewActivity extends AppCompatActivity {

    //Layout Objects
    private TextView bookTitleLabel;
    private TextView bookAuthorLabel;
    private TextView bookDescriptionLabel;
    private TextView isbnLabel;
    private TextView ownerLabel;
    private TextView statusLabel;
    private Button requestButton;
    private Button viewRequestsButton;
    private Button deleteButton;

    private BookListing listing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_listing_view);

        //Get References to Layout Objects
        bookTitleLabel = findViewById(R.id.bookTitleLabel);
        bookAuthorLabel = findViewById(R.id.bookAuthorLabel);
        bookDescriptionLabel = findViewById(R.id.bookDescriptionLabel);
        isbnLabel = findViewById(R.id.isbnLabel);
        ownerLabel = findViewById(R.id.ownerLabel);
        statusLabel = findViewById(R.id.statusLabel);
        requestButton = findViewById(R.id.requestButton);
        viewRequestsButton = findViewById(R.id.viewRequestsButton);
        deleteButton = findViewById(R.id.deleteButton);

        //Get Intent
        Intent intent = getIntent();
        //Check if given info to fetch listing
        if (intent.hasExtra("username") && intent.hasExtra("bookisbn")) {
            String username = intent.getStringExtra("username");
            String isbn = intent.getStringExtra("bookisbn");
            listing = MockDatabase.getInstance().readBookListing(username, isbn, MockDatabase.OWNEDLIBRARY);
        }


        viewRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRequests(listing);
            }
        });

        //Copied from Jamie's assignment 1
        //Create the dialog for the Delete button
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Are you sure you want to delete this item?");
        alertBuilder.setTitle("Confirm");
        alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Delete
                Toast.makeText(getApplicationContext(), "Deleted Item", Toast.LENGTH_SHORT).show();
                MockDatabase.getInstance().removeBookListing(listing);
                finish();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "Did not delete.", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Confirm Delete with a Dialog
                alertBuilder.create().show();
            }
        });
    }

    /**
     * Fills the layout with the data in the listing
     */
    private void fillLayout(){
        if (listing == null) {
            Toast.makeText(this, "Listing Not Found", Toast.LENGTH_LONG).show();
        } else {
            bookTitleLabel.setText(listing.getBook().getTitle());
            bookAuthorLabel.setText(listing.getBook().getAuthor());
            isbnLabel.setText(listing.getBook().getIsbn());
            ownerLabel.setText(listing.getOwnerUsername().getUsername());
            statusLabel.setText(listing.getStatus().toString());
        }
    }

    /**
     * Start an activity to view this listing's requests.
     * @param item
     */
    private void viewRequests(BookListing item) {
        Intent intent = new Intent(this, RequestsViewActivity.class);

        intent.putExtra("username", item.getOwnerUsername().getUsername());
        intent.putExtra("bookisbn", item.getBook().getIsbn());
        startActivity(intent);
    }

}
