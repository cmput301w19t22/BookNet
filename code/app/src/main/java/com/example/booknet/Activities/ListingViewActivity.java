package com.example.booknet.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.Constants.BookListingStatus;
import com.example.booknet.Constants.NotificationType;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Dialogs.DialogCloseListener;
import com.example.booknet.Dialogs.OthersProfileViewCard;
import com.example.booknet.Dialogs.VerifyBorrowDialog;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.CurrentUser;
import com.example.booknet.Model.InAppNotification;
import com.example.booknet.R;


/**
 * Activity to display another user's BookListing.
 * Contains controls to request the book.
 *
 * @author Jamie
 * @version 1.0
 */
public class ListingViewActivity extends AppCompatActivity implements DialogCloseListener {

    //Layout Objects
    private ImageView bookThumbnail;
    private TextView bookTitleLabel;
    private TextView bookAuthorLabel;
    private TextView bookDescriptionLabel;
    private TextView isbnLabel;
    private TextView ownerLabel;
    private TextView statusLabel;
    private Button requestButton;
    private Button verifyButton;
    private Button ownerProfileButton;
    private ConstraintLayout geoLocationBlock;
    private Button setLocationButton;
    private Button viewLocationButton;
    private TextView geolocationLabel;
    private ImageButton backButton;
    private DatabaseManager manager = DatabaseManager.getInstance();

    //Activity Data
    private BookListing listing;

    private boolean alreadyRequested;
    private String intentIsbn;
    private int intentDupId;

    private String CHANNEL_ID = "BOOKNET_NOTIFICATION";

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
        bookThumbnail = findViewById(R.id.bookThumbnail);
        bookTitleLabel = findViewById(R.id.BookTitleLabel);
        bookAuthorLabel = findViewById(R.id.bookAuthorLabel);
        bookDescriptionLabel = findViewById(R.id.bookDescriptionLabel);
        isbnLabel = findViewById(R.id.isbnLabel);
        ownerLabel = findViewById(R.id.ownerLabel);
        statusLabel = findViewById(R.id.statusLabel);
        requestButton = findViewById(R.id.requestButton);
        verifyButton = findViewById(R.id.verifyButton);
        ownerProfileButton = findViewById(R.id.ownerProfileButton);
        geoLocationBlock = findViewById(R.id.geoLocationBlock);
        setLocationButton = findViewById(R.id.setLocationButton);
        viewLocationButton = findViewById(R.id.viewLocationButton);
        geolocationLabel = findViewById(R.id.geolocationLabel);
        backButton = findViewById(R.id.backButton);
        bookTitleLabel.setSelected(true);//select so it scrolls
        bookAuthorLabel.setSelected(true);


        //Get Intent
        Intent intent = getIntent();
        //Check if given info to fetch listing
        if (intent.hasExtra("ownerUsername") && intent.hasExtra("isbn")) {
            String username = intent.getStringExtra("ownerUsername");
            intentIsbn = intent.getStringExtra("isbn");
            intentDupId = intent.getIntExtra("dupID", 0);
            listing = manager.readBookListingOfUsername(username, intentIsbn, intentDupId);
        }

        //if (listing.getOwnerUsername().equals(CurrentUser.getInstance().getUsername())) {
        //    requestButton.setVisibility(View.GONE);
        //}

        //Fill Layout
        updateLayout(listing);

        //#region Listeners
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.ifListingRequestedByCurrentUser(listing)) {
                    if (listing.getStatus() == BookListingStatus.Borrowed) {
                        try {
                            sendReturnRequest();
                            Toast.makeText(getApplicationContext(), "Owner Notified For Return", Toast.LENGTH_SHORT).show();
                        } catch (DatabaseManager.DatabaseException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            sendRemoveRequest();
                            Toast.makeText(getApplicationContext(), "Your request is cancelled", Toast.LENGTH_SHORT).show();
                        } catch (DatabaseManager.DatabaseException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    try {
                        sendAddRequest();
                        Log.d("mattTag", "outta there");
                        Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_SHORT).show();
                        Log.d("mattTag", "toast works good");
                    } catch (DatabaseManager.DatabaseException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

//                Log.d("mattTag", "finishing");
                //finish();
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listing.getStatus() == BookListingStatus.Accepted) {
                    verifyBorrow();
                } else if (listing.getStatus() == BookListingStatus.Borrowed) {
                    verifyReturn();
                }
            }
        });

        ownerProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOwnerProfile();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //#endregion

        //#region GeoLocation
        setLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGeoLocation(true);
            }
        });

        viewLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGeoLocation(false);
            }
        });
        //#endregion

        /*RotateAnimation anim2 = new RotateAnimation(45f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        ScaleAnimation anim1 = new ScaleAnimation(0.1f,1f,0.1f,1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        anim1.setDuration(700);
        anim1.setInterpolator(new AnticipateOvershootInterpolator());
        findViewById(R.id.listingView).startAnimation(anim1);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listing != null) {
            updateLayout(listing);
        } else {
            Log.d("jamie", "Listing was null, attempt to read from db again");
            listing = manager.readUserOwnedBookListing(intentIsbn, intentDupId);
        }
    }

    @Override
    public void onDialogClose() {

        listing = manager.readUserOwnedBookListing(intentIsbn, intentDupId);
        updateLayout(listing);
    }

    /**
     * Updates the contents of the layout objects. Called when creating the activity
     * and should be called whenever the listing data changes.
     *
     * @param listing
     */
    private void updateLayout(BookListing listing) {
        bookTitleLabel.setText(listing.getBook().getTitle());
        bookAuthorLabel.setText(listing.getBook().getAuthor());
        isbnLabel.setText(listing.getBook().getIsbn());
        ownerLabel.setText(listing.getOwnerUsername());
        statusLabel.setText(listing.getStatus().toString());

        Bitmap bitmap = manager.getCachedThumbnail(listing);
        if (bitmap != null) {
            bookThumbnail.setImageBitmap(bitmap);
        } else {
            bookThumbnail.setImageResource(R.drawable.ic_book_default);
        }

        //Layouts for an Accepted Borrower
        geoLocationBlock.setVisibility(View.GONE);
        verifyButton.setVisibility(View.GONE);
        if (CurrentUser.getInstance().isMe(listing.getBorrowerName())) {
            //Decide if showing geolocation
            if (listing.getStatus() == BookListingStatus.Accepted) {
                //Show geolocation stuff
                geoLocationBlock.setVisibility(View.VISIBLE);
            }
            //Decide if show verify button
            if (listing.getStatus() == BookListingStatus.Accepted
                    || listing.getStatus() == BookListingStatus.Borrowed) {
                verifyButton.setVisibility(View.VISIBLE);
            }
        }

        //Manage Request Button
        if (manager.ifListingRequestedByCurrentUser(listing)) {
            if (listing.getStatus() == BookListingStatus.Borrowed) {
                requestButton.setEnabled(true);
                requestButton.setText("Return");
            } else {
                requestButton.setEnabled(true);
                requestButton.setText("Cancel");
            }
        } else {
            if (listing.getStatus() == BookListingStatus.Accepted
                    || listing.getStatus() == BookListingStatus.Borrowed || manager.belongsToUser(listing, CurrentUser.getInstance().getUID())) {
                requestButton.setEnabled(false);
                requestButton.setVisibility(View.GONE);
            } else {
                requestButton.setEnabled(true);
            }
            requestButton.setText("Request");

        }
    }

    /**
     * Creates a request for this book listing from the current user.
     */
    private void sendAddRequest() throws DatabaseManager.DatabaseException {
        manager.requestBookListing(listing);

        recreate();
        Log.d("jamie","recreate");
    }

    /**
     * Cancels a request for this book listing from the current user.
     */
    private void sendRemoveRequest() throws DatabaseManager.DatabaseException {
        manager.cancelRequestForListing(listing);
        recreate();
    }

    /**
     * Requests to return the book for this book listing.
     */
    private void sendReturnRequest() throws DatabaseManager.DatabaseException {
        manager.writeNotification(new InAppNotification(listing, listing.getOwnerUsername(),
                listing.getBorrowerName(), NotificationType.wantsReturn));
    }

    /**
     * Initiates a dialog to verify the borrow by scanning
     */
    private void verifyBorrow() {
        VerifyBorrowDialog verifyBorrowDialog = VerifyBorrowDialog.newInstance(listing, false);
        verifyBorrowDialog.show(getSupportFragmentManager(), "Verify Borrow");
    }

    /**
     * Initiates a dialog to verify the return by scanning
     */
    private void verifyReturn() {
        VerifyBorrowDialog verifyBorrowDialog = VerifyBorrowDialog.newInstance(listing, false);
        verifyBorrowDialog.show(getSupportFragmentManager(), "Verify Return");
    }


    /**
     * Starts an activity to view the profile of the user whoo owns this book.
     */
    private void viewOwnerProfile() {
        OthersProfileViewCard profileCard = OthersProfileViewCard.newInstance(listing);
        profileCard.show(getSupportFragmentManager(), "othersProfileDialog");
    }

    /**
     * Starts a dialog to select or view a geolocation.
     * The user will be able to set a position if editmode is on.
     *
     * @param editmode Whether to open the map in edit mode
     */
    private void openGeoLocation(boolean editmode) {
        //Toast.makeText(getApplicationContext(), "Select a Location", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MapSelectActivity.class);
        intent.putExtra("username", listing.getOwnerUsername());
        intent.putExtra("bookisbn", listing.getBook().getIsbn());
        intent.putExtra("dupID", listing.getDupInd());
        intent.putExtra("editmode", editmode);
        startActivity(intent);
    }


    /**
     * Gets the result and passes it up so nested fragments can get it.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
