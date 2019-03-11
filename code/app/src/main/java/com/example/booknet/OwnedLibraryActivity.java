package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

/**
 * An activity to display the owned library of a user.
 *
 * @author Jamie
 * @version 1.0
 */
public class OwnedLibraryActivity extends AppCompatActivity {

    //Layout Objects
    private RecyclerView libraryListView;
    private OwnedListingAdapter listingAdapter;
    private Button addButton;

    //Activity Data
    private BookLibrary library;

    /**
     * Called when creating the activity.
     * Sets a click listener for the add button
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_library);

        //Add Click Listener
        addButton = findViewById(R.id.addBookButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

        //Get Data From the Database
        //todo get real library
        library = MockDatabase.getInstance().readUserOwnedLibrary(CurrentUser.getInstance().getUserAccount().getUsername());

        fillLayout();//todo delete when using real db

    }

    /**
     * Called when the activity starts
     * Tells the list's adapter to update.
     */
    @Override
    protected void onStart() {
        super.onStart();

        //Update List Data
        listingAdapter.notifyDataSetChanged();
    }

    /**
     * Fills the activity layout from the data.
     */
    private void fillLayout() {
        //Create fake data if there's no real data.
        //todo remove block
        if (library == null) {
            Book b1 = new Book("Fake Book 1", "Author 1", "", "1234567890");
            Book b2 = new Book("Fake Book 2", "Author 2", "", "1234567891");
            UserAccount currentUserAccount = CurrentUser.getInstance().getUserAccount().clone();
            library.addBookListing(new BookListing(b1, currentUserAccount));
            library.addBookListing(new BookListing(b2, currentUserAccount));
        }

        //Setup RecyclerView
        libraryListView = findViewById(R.id.bookLibrary);
        libraryListView.setLayoutManager(new LinearLayoutManager(this));
        listingAdapter = new OwnedListingAdapter(library, this);
        libraryListView.setAdapter(listingAdapter);
    }

    /**
     * Starts the activity to add a new book
     */
    private void addBook() {
        Intent intent = new Intent(this, NewBookActivity.class);
        startActivity(intent);
    }
}
