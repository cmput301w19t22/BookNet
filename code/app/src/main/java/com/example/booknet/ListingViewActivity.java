package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.Constants.BookListingStatus;


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
    private ConstraintLayout geoLocationBlock;
    private Button setLocationButton;
    private ImageView viewLocationButton;
    private TextView geolocationLabel;
    private DatabaseManager manager = DatabaseManager.getInstance();

    //Activity Data
    private BookListing listing;

    private boolean alreadyRequested;

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
        bookTitleLabel = findViewById(R.id.BookTitleLabel);
        bookAuthorLabel = findViewById(R.id.bookAuthorLabel);
        bookDescriptionLabel = findViewById(R.id.bookDescriptionLabel);
        isbnLabel = findViewById(R.id.isbnLabel);
        ownerLabel = findViewById(R.id.ownerLabel);
        statusLabel = findViewById(R.id.statusLabel);
        requestButton = findViewById(R.id.requestButton);
        ownerProfileButton = findViewById(R.id.ownerProfileButton);
        geoLocationBlock = findViewById(R.id.geoLocationBlock);
        setLocationButton = findViewById(R.id.setLocationButton);
        viewLocationButton = findViewById(R.id.viewLocationButton);
        geolocationLabel = findViewById(R.id.geolocationLabel);
        bookTitleLabel.setSelected(true);//select so it scrolls
        bookAuthorLabel.setSelected(true);


        //Get Intent
        Intent intent = getIntent();
        //Check if given info to fetch listing
        if (intent.hasExtra("username") && intent.hasExtra("bookisbn")) {
            String username = intent.getStringExtra("username");
            String isbn = intent.getStringExtra("bookisbn");
            int dupID = intent.getIntExtra("dupID", 0);
            listing = manager.readBookListingOfUsername(username, isbn, dupID);
        }

        //Fill Layout
        bookTitleLabel.setText(listing.getBook().getTitle());
        bookAuthorLabel.setText(listing.getBook().getAuthor());
        isbnLabel.setText(listing.getBook().getIsbn());
        ownerLabel.setText(listing.getOwnerUsername());
        statusLabel.setText(listing.getStatus().toString());

        if (manager.ifListingRequestedByCurrentUser(listing)) {
            requestButton.setText("Cancel Request");

        } else {
            requestButton.setText("Request");

        }

        geoLocationBlock.setVisibility(View.GONE);
        if (listing.getStatus() == BookListingStatus.Accepted) {
            if (CurrentUser.getInstance().isMe(listing.getBorrowerName())) {
                //Show geolocation stuff
                geoLocationBlock.setVisibility(View.VISIBLE);
            }
        }

        //#region Listeners
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.ifListingRequestedByCurrentUser(listing)) {
                    try {
                        sendRemoveRequest();
                        Toast.makeText(getApplicationContext(), "Your request is cancelled", Toast.LENGTH_SHORT).show();
                    } catch (DatabaseManager.DatabaseException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    try {
                        sendAddRequest();
                        Log.d("mattTag", "outta there");
                        Toast.makeText(getApplicationContext(), "Requested", Toast.LENGTH_SHORT).show();
                        Log.d("mattTag", "toast works good");
                    } catch (DatabaseManager.DatabaseException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

//                Log.d("mattTag", "finishing");
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

        //#region GeoLocation
        setLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGeoLocation();
                //todo allow borrower to set location for return
            }
        });

        viewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewGeoLocation();
            }
        });
        //#endregion
    }


    /**
     * Creates a request for this book listing from the current user.
     */
    private void sendAddRequest() throws DatabaseManager.DatabaseException {
        manager.requestBookListing(listing);
    }

    /**
     * Creates a request for this book listing from the current user.
     */
    private void sendRemoveRequest() throws DatabaseManager.DatabaseException {
        manager.requestBookListingRemoval(listing);
    }

    /**
     * Starts an activity to view the profile of the user whoo owns this book.
     */
    private void viewOwnerProfile() {
        OthersProfileViewCard profileCard = OthersProfileViewCard.newInstance(listing);
        profileCard.show(getSupportFragmentManager(), "othersProfileDialog");
    }

    /**
     * Starts a dialog to select a geolocation.
     */
    private void setGeoLocation() {
        Toast.makeText(getApplicationContext(), "Select a Location\nTO BE IMPLEMENTED", Toast.LENGTH_SHORT).show();
        //todo implement
    }

    /**
     * Starts a dialog to view the geolocation
     */
    private void viewGeoLocation() {
        Toast.makeText(getApplicationContext(), "View GeoLocation Not Implemented", Toast.LENGTH_SHORT).show();
        //todo implement
    }
}
