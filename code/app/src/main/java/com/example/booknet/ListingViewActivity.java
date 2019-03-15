package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Activity to display another user's BookListing.
 * Contains controls to request the book.
 *
 * @author Jamie
 * @version 1.0
 */
public class ListingViewActivity extends AppCompatActivity {

    //Layout Objects
    private TextView bookTitleLabel;
    private TextView bookAuthorLabel;
    private TextView bookDescriptionLabel;
    private TextView isbnLabel;
    private TextView ownerLabel;
    private TextView statusLabel;
    private Button requestButton;
    private Button ownerProfileButton;
    private DatabaseManager manager=DatabaseManager.getInstance();

    //Activity Data
    private BookListing listing;

    /**
     * Called when creating the activity.
     * Performs the following tasks:
     * - Obtain the layout object refs
     * - Obtain the intent for obtaining the listing
     * - Setup click listeners for the controls
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_view);

        //Get References to Layout Objects
        bookTitleLabel = findViewById(R.id.bookTitleLabel);
        bookAuthorLabel = findViewById(R.id.bookAuthorLabel);
        bookDescriptionLabel = findViewById(R.id.bookDescriptionLabel);
        isbnLabel = findViewById(R.id.isbnLabel);
        ownerLabel = findViewById(R.id.ownerLabel);
        statusLabel = findViewById(R.id.statusLabel);
        requestButton = findViewById(R.id.requestButton);
        ownerProfileButton = findViewById(R.id.ownerProfileButton);

        //Get Intent
        Intent intent = getIntent();
        //Check if given info to fetch listing
        if (intent.hasExtra("username") && intent.hasExtra("bookisbn")) {
            String username = intent.getStringExtra("username");
            String isbn = intent.getStringExtra("bookisbn");
            listing = manager.readBookListingWithISBN(isbn);
        }
        fillLayout();//todo delete when using real db

        //#region Listeners
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        ownerProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOwnerProfile();
            }
        });
        //#endregion
    }

    /**
     * Fills the layout with the data in the listing
     */
    private void fillLayout() {
        if (listing == null) {
            Toast.makeText(this, "Listing Not Found", Toast.LENGTH_LONG).show();
        } else {
            bookTitleLabel.setText(listing.getBook().getTitle());
            bookAuthorLabel.setText(listing.getBook().getAuthor());
            isbnLabel.setText(listing.getBook().getIsbn());
            ownerLabel.setText(listing.getOwnerUsername());
            statusLabel.setText(listing.getStatus().toString());
        }
    }

    /**
     * Creates a request for this book listing from the current user.
     */
    // todo: fix request, MockdataBase deprecated
    private void sendRequest() {
//        MockDatabase.getInstance().addRequestToListing(listing, CurrentUser.getInstance().getUserAccount().getUsername());
    }

    /**
     * Starts an activity to view the profile of the user whoo owns this book.
     */
    private void viewOwnerProfile() {
        Intent intent = new Intent(this, UserProfileViewActivity.class);
        intent.putExtra("username", listing.getOwnerUsername());
        startActivity(intent);
    }
}
