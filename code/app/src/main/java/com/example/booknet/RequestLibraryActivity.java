package com.example.booknet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

/**
 * An activity to display the requested library of a user.
 *
 * @author Jamie
 * @version 1.0
 */
public class RequestLibraryActivity extends AppCompatActivity {

    //Layout Objects
    private RecyclerView libraryListView;
    private OwnedLibraryAdapter listingAdapter;
    private Button addButton;

    //Activity Data
    private BookLibrary library;

    /**
     * Called when creating the activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_library);

        //Add Click Listener
        addButton = findViewById(R.id.addBookButton);
        addButton.setVisibility(View.GONE);//Dont use this button

        //Get Data From the Database
        //todo get real library
        library = MockDatabase.getInstance().readUserRequests(CurrentUser.getInstance().getUserAccount().getUsername());

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

        //Setup RecyclerView
        libraryListView = findViewById(R.id.bookLibrary);
        libraryListView.setLayoutManager(new LinearLayoutManager(this));
        listingAdapter = new OwnedLibraryAdapter(library, this);
        libraryListView.setAdapter(listingAdapter);
    }
}
