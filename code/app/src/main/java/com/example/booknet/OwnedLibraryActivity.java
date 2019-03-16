package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
    private ValueEventListener valueEventListener = null;

    //Activity Data
    private BookLibrary library;
    private BookLibrary filteredLibrary = new BookLibrary();
    private ValueEventListener listener;

    DatabaseManager manager = DatabaseManager.getInstance();

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

        //Get Data From the Database, library will get auto updated (it's magic babe)
        library = manager.readUserOwnedLibrary();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // once the data is changed, we just change our corresponding static variable

                //first empty it
                filteredLibrary.removeAllBooks();

                // then fill it as it is in the database
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    BookListing bookListing = data.getValue(BookListing.class);
                    if (bookListing != null) {
                        filteredLibrary.addBookListing(bookListing.clone());
                    }
                }

                listingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };

        manager.getUserLisitngsRef().addValueEventListener(listener);

        filteredLibrary = library.clone();


        Log.d("matt", "creating new adpator");

        libraryListView = findViewById(R.id.bookLibrary);
        libraryListView.setLayoutManager(new LinearLayoutManager(this));
        listingAdapter = new OwnedListingAdapter(filteredLibrary, this);
        libraryListView.setAdapter(listingAdapter);

        Spinner filter = findViewById(R.id.spinner);

        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView selectedView = (TextView) view;
                String selectedItem = selectedView.getText().toString();
                if (selectedItem.equals("All")) {
                    Log.d("mattTag", "copying one by one");
                    filteredLibrary.copyOneByOne(library);
                    Log.d("mattTag", "after copying: " + filteredLibrary.toString());
                } else {
                    Log.d("mattTag", "yi");
                    filteredLibrary.filterByStatus(library, BookListing.Status.valueOf(selectedItem));
                }

                listingAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    /**
     * Called when the activity starts
     * Tells the list's adapter to update.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("mattTag", "starting the activity, notifying");
        Log.d("mattTag", "when starting, the books are: " + filteredLibrary.toString());
        //Update List Data
        listingAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        Log.d("mattTag", "DESTROIIEIIEIIII");
        manager.getUserLisitngsRef().removeEventListener(listener);
        super.onDestroy();

    }

    public void notifyDataSetChanged() {
        Log.d("mattTag", "activity has the notification");
        listingAdapter.notifyDataSetChanged();
    }

    /**
     * Starts the activity to add a new book
     */
    private void addBook() {
        Intent intent = new Intent(this, NewBookActivity.class);
        startActivity(intent);
    }


}
