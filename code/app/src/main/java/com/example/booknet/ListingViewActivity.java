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
    private DatabaseManager manager = DatabaseManager.getInstance();

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
        bookTitleLabel.setSelected(true);//select so it scrolls
        bookAuthorLabel.setSelected(true);


        //Get Intent
        Intent intent = getIntent();
        //Check if given info to fetch listing
        if (intent.hasExtra("username") && intent.hasExtra("bookisbn")) {
            String username = intent.getStringExtra("username");
            String isbn = intent.getStringExtra("bookisbn");
            listing = manager.readBookListingWithUIDAndISBN(CurrentUser.getInstance().getUID(), isbn);
        }

        bookTitleLabel.setText(listing.getBook().getTitle());
        bookAuthorLabel.setText(listing.getBook().getAuthor());
        isbnLabel.setText(listing.getBook().getIsbn());
        ownerLabel.setText(listing.getOwnerUsername());
        statusLabel.setText(listing.getStatusString());

        //#region Listeners
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                //startActivity(new Intent(ListingViewActivity.this, BookSearchFragment.class));
                finish();
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
     * Creates a request for this book listing from the current user.
     */
    private void sendRequest() {
        boolean res = manager.requestBookListing(listing, CurrentUser.getInstance().getUserAccount().getUsername());
        if (res){
            Toast.makeText(this, "book requested", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Starts an activity to view the profile of the user whoo owns this book.
     */
    private void viewOwnerProfile() {
        Intent intent = new Intent(this, UserProfileViewFragment.class);
        if (listing != null) {
            intent.putExtra("username", listing.getOwnerUsername());
        }
        startActivity(intent);
    }
}
