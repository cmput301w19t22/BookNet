package com.example.booknet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

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
    private ProgressBar progressBar;
    private BookListingAdapter listingAdapter;
    private DatabaseManager manager = DatabaseManager.getInstance();


    //App Data
    BookLibrary allBookListings;


    /**
     * Called when the activity is created.
     * Sets up the search results list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //todo get real search results


        allBookListings = manager.readAllBookListings();

        searchResults = findViewById(R.id.searchResults);
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        listingAdapter = new BookListingAdapter(allBookListings, this);
        searchResults.setAdapter(listingAdapter);


        //bookListings = MockDatabase.getInstance().readAllBookListings();

    }

    public void addListingToList(BookListing listing) {
        allBookListings.addBookListing(listing);
        listingAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }


    /**
     * Fills the recycler view list with the search data
     */
    private void fillLayout() {
        //Setup RecyclerView
        searchResults = findViewById(R.id.searchResults);
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        listingAdapter = new BookListingAdapter(allBookListings, this);
        searchResults.setAdapter(listingAdapter);
        //Deactivate the progress bar

    }

}
