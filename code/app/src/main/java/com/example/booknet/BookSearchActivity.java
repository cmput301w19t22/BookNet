package com.example.booknet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Activity to search the database for available books.
 * Without searching, it will display all books in the database.
 *
 * @author Jamie
 * @version 1.0
 */
public class BookSearchActivity extends AppCompatActivity {

    //Layout Objects
    private RecyclerView searchResults;
    private BookListingAdapter listingAdapter;
    private DatabaseManager databaseManager;

    //App Data
    ArrayList<BookListing> bookListings;

    /**
     * Called when the activity is created.
     * Sets up the search results list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        //todo get real search results
        //temp fake results
        bookListings = new ArrayList<>();

        searchResults = findViewById(R.id.searchResults);
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        listingAdapter = new BookListingAdapter(bookListings, this);
        searchResults.setAdapter(listingAdapter);

        databaseManager = new DatabaseManager();
        databaseManager.readAllBookListings(this);

        //bookListings = MockDatabase.getInstance().readAllBookListings();

    }

    public void addListingToList(BookListing listing){
        bookListings.add(listing);
        listingAdapter.notifyDataSetChanged();
    }


    /**
     * Fills the recycler view list with the search data
     */
    private void fillLayout() {
        //Setup RecyclerView
        searchResults = findViewById(R.id.searchResults);
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        listingAdapter = new BookListingAdapter(bookListings, this);
        searchResults.setAdapter(listingAdapter);
    }

}
