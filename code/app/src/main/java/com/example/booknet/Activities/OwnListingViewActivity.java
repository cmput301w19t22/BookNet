package com.example.booknet.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.Constants.BookListingStatus;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Dialogs.DialogCloseListener;
import com.example.booknet.Dialogs.EditBookDialog;
import com.example.booknet.Dialogs.PhotoEditDialog;
import com.example.booknet.Dialogs.VerifyBorrowDialog;
import com.example.booknet.Model.BookListing;
import com.example.booknet.R;

/**
 * Activity to view a listing that the current user owns.
 *
 * @author Jamie
 * @version 1.0
 */
public class OwnListingViewActivity extends AppCompatActivity implements DialogCloseListener {

    //Layout Objects
    private ImageView photoThumbnail;
    private TextView bookTitleLabel;
    private TextView bookAuthorLabel;
    private TextView bookDescriptionLabel;
    private TextView isbnLabel;
    private TextView statusLabel;
    private TextView requestsCountCircle;
    private Button requestButton;
    private Button viewRequestsButton;
    private Button verifyButton;
    private ImageButton deleteButton;
    private ImageButton editButton;
    private ImageButton editPhotoButton;
    private ConstraintLayout geoLocationBlock;
    private Button setLocationButton;
    private ImageView viewLocationButton;
    private TextView geolocationLabel;
    private ImageButton backButton;
    private PhotoEditDialog photoEditDialog;
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

        //#region Get References to Layout Objects
        photoThumbnail = findViewById(R.id.bookThumbnail);
        bookTitleLabel = findViewById(R.id.BookTitleLabel);
        bookAuthorLabel = findViewById(R.id.bookAuthorLabel);
        bookDescriptionLabel = findViewById(R.id.bookDescriptionLabel);
        isbnLabel = findViewById(R.id.isbnLabel);
        statusLabel = findViewById(R.id.statusLabel);
        requestsCountCircle = findViewById(R.id.requestsNum);
        requestButton = findViewById(R.id.requestButton);
        viewRequestsButton = findViewById(R.id.viewRequestsButton);
        verifyButton = findViewById(R.id.verifyButton);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        editPhotoButton = findViewById(R.id.editPhotoButton);
        geoLocationBlock = findViewById(R.id.geoLocationBlock);
        setLocationButton = findViewById(R.id.setLocationButton);
        viewLocationButton = findViewById(R.id.viewLocationButton);
        geolocationLabel = findViewById(R.id.geolocationLabel);
        backButton = findViewById(R.id.backButton);
        bookTitleLabel.setSelected(true);//select to enable scrolling
        bookAuthorLabel.setSelected(true);
        //geoLocationBlock.setVisibility(View.GONE);//Deactivate this section unless accepted
        //#endregion


        Intent intent = getIntent();
        //Check if given info to fetch listing
        if (intent.hasExtra("isbn")) {
            String isbn = intent.getStringExtra("isbn");
            int dupID = intent.getIntExtra("dupID", 0);

            listing = manager.readUserOwnedBookListing(isbn, dupID);
        }

        //Fill Layout
        updateLayout(listing);

        // todo: fix edit
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

        //#region Photos
        View.OnClickListener editPhotoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhoto();
            }
        };

        editPhotoButton.setOnClickListener(editPhotoListener);


        //Copied from Jamie's assignment 1
        //Create the dialog for the Delete button
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Confirm");
        alertBuilder.setMessage("Are you sure you want to delete this item?");
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //endregion

        //#region GeoLocation
        setLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGeoLocation();
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

    @Override
    public void onDialogClose() {
        updateLayout(listing);
    }

    /**
     * Updates the contents of the layout objects. Called when creating the activity
     * and should be called whenever the listing data changes.
     *
     * @param listing
     */
    private void updateLayout(BookListing listing) {
        Log.d("jamie", "update own listing layout");
        bookTitleLabel.setText(listing.getBook().getTitle());
        bookAuthorLabel.setText(listing.getBook().getAuthor());
        isbnLabel.setText(listing.getBook().getIsbn());
        statusLabel.setText(listing.getStatus().toString());

        Bitmap thumbnail = listing.getPhotoBitmap();
        if (thumbnail != null) {
            photoThumbnail.setImageBitmap(thumbnail);
        } else {
            photoThumbnail.setImageResource(R.drawable.ic_book_default);
        }

        //Decide which controls to show
        if (listing.getStatus() == BookListingStatus.Accepted
                || listing.getStatus() == BookListingStatus.Borrowed) {
            verifyButton.setVisibility(View.VISIBLE);
            viewRequestsButton.setVisibility(View.GONE);
            requestsCountCircle.setVisibility(View.GONE);
            geoLocationBlock.setVisibility(View.VISIBLE);

        } else {
            verifyButton.setVisibility(View.GONE);
            geoLocationBlock.setVisibility(View.GONE);
            //Requests View Block
            int numRequests = listing.getRequests().size();
            if (numRequests > 0) {
                if (numRequests > R.integer.notificationCircleMax) {
                    //Limit the value that can appear on the circle
                    requestsCountCircle.setText(R.integer.notificationCircleMax + "+");
                } else {
                    requestsCountCircle.setText(Integer.toString(numRequests));
                }
                requestsCountCircle.setVisibility(View.VISIBLE);
            } else {

                requestsCountCircle.setVisibility(View.GONE);
                //viewRequestsButton.setEnabled(false);
                viewRequestsButton.setVisibility(View.GONE);
                //viewLocationButton.setVisibility(View.GONE);
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
            /*Intent intent = new Intent(this, EditBookDialog.class);
            intent.putExtra("dupInd", item.getDupInd());
            intent.putExtra("isbn", item.getBook().getIsbn());
            startActivity(intent);*/
            EditBookDialog editBookDialog = EditBookDialog.newInstance(item);
            editBookDialog.show(getSupportFragmentManager(), "Edit Book");
        }
    }

    /**
     * Start an activity for editing the photo for this listing.
     */
    private void editPhoto() {
        photoEditDialog = PhotoEditDialog.newInstance(listing);
        DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        };
        photoEditDialog.show(getSupportFragmentManager(), "Edit Photo");
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
            intent.putExtra("dupID", item.getDupInd());

            startActivity(intent);
        }
    }

    /**
     * Initiates a dialog to verify the borrow by scanning
     */
    private void verifyBorrow() {
        VerifyBorrowDialog verifyBorrowDialog = VerifyBorrowDialog.newInstance(listing, true);
        verifyBorrowDialog.show(getSupportFragmentManager(), "Verify Borrow");
    }

    /**
     * Initiates a dialog to verify the return by scanning
     */
    private void verifyReturn() {
        VerifyBorrowDialog verifyBorrowDialog = VerifyBorrowDialog.newInstance(listing, true);
        verifyBorrowDialog.show(getSupportFragmentManager(), "Verify Return");
    }

    /**
     * Starts a dialog to select a geolocation.
     */
    private void setGeoLocation() {
        Toast.makeText(getApplicationContext(), "Select a Location", Toast.LENGTH_SHORT).show();
        //todo implement
        Intent intent = new Intent(this, MapSelectActivity.class);
        startActivity(intent);
    }

    /**
     * Starts a dialog to view the geolocation
     */
    private void viewGeoLocation() {
        Toast.makeText(getApplicationContext(), "View GeoLocation Not Implemented", Toast.LENGTH_SHORT).show();
        //todo implement
    }

    /**
     * Gets the result and passes it up so nested fragments can get it.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("jamie", "permissions return");


        for (int r : grantResults) {
            if (r != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Not Granted\nCannot use feature", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (requestCode == 1) {
            photoEditDialog.requestTakePhoto();
        }
        if (requestCode == 2) {
            photoEditDialog.selectImageFromFile();
        }
    }


}
