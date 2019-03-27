package com.example.booknet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

/**
 * Activity to view a listing that the current user owns.
 *
 * @author Jamie
 * @version 1.0
 */
public class OwnListingViewActivity extends AppCompatActivity {

    //Layout Objects
    private TextView bookTitleLabel;
    private TextView bookAuthorLabel;
    private TextView bookDescriptionLabel;
    private TextView isbnLabel;
    private TextView ownerLabel;
    private TextView statusLabel;
    private TextView requestCountLabel;
    private Button requestButton;
    private Button viewRequestsButton;
    private ImageButton deleteButton;
    private ImageButton editButton;
    private ImageButton editPhotoButton;
    private ConstraintLayout geoLocationBlock;
    private Button setLocationButton;
    private ImageView viewLocationButton;
    private TextView geolocationLabel;
    private DatabaseManager manager = DatabaseManager.getInstance();

    //Activity Data
    private BookListing listing;

    /**
     * Called when the activity is created.
     * Performs the following actions:
     * - Gets the intent
     * - Sets listeners for the controls
     *
     * @param savedInstanceState
     */
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
        requestCountLabel = findViewById(R.id.requestCountLabel);
        requestButton = findViewById(R.id.requestButton);
        viewRequestsButton = findViewById(R.id.viewRequestsButton);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        bookTitleLabel.setSelected(true);//select to enable scrolling
        bookAuthorLabel.setSelected(true);

        //Get Intent
        Intent intent = getIntent();
        //Check if given info to fetch listing
        if (intent.hasExtra("bookisbn")) {
            String isbn = intent.getStringExtra("bookisbn");
            listing = manager.readUserOwnedBookListingWithISBN(isbn);

            fillLayout();
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBook(listing);
            }
        });

        //Set Listener for ViewRequests Button
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
                manager.removeBookListing(listing);
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

    @Override
    protected void onStart() {
        super.onStart();

        fillLayout();
    }

    /**
     * Fills the activity layout from the listing.
     */
    private void fillLayout() {
        if (listing != null) {
            bookTitleLabel.setText(listing.getBook().getTitle());
            bookAuthorLabel.setText(listing.getBook().getAuthor());
            isbnLabel.setText(listing.getBook().getIsbn());
            ownerLabel.setText(listing.getOwnerUsername());
            statusLabel.setText(listing.getStatus().toString());
            int numRequests = listing.getRequests().size();
            if (numRequests > 0) {
                requestCountLabel.setText(numRequests);
            } else {
                //requestCountLabel.setVisibility(View.INVISIBLE);//todo ???
            }
        }
    }


    /**
     * Start an activity to edit the book for this listing.
     *
     * @param item
     */
    private void editBook(BookListing item) {
        if (item != null) {
            Intent intent = new Intent(this, EditBookActivity.class);
            intent.putExtra("username", item.getOwnerUsername());
            intent.putExtra("bookisbn", item.getBook().getIsbn());
            startActivity(intent);
        }
    }


    /**
     * Start an activity to view this listing's requests.
     *
     * @param item
     */
    private void viewRequests(BookListing item) {
        Intent intent = new Intent(this, RequestsViewActivity.class);
        if (item != null) {
            intent.putExtra("username", item.getOwnerUsername());
            intent.putExtra("bookisbn", item.getBook().getIsbn());
            startActivity(intent);
        }
    }

}
