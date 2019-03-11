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
 *
 * @author Jamie
 * @version 1.0
 */
public class BookSearchActivity extends AppCompatActivity {

    //Layout Objects
    private RecyclerView searchResults;
    private BookListingAdapter listingAdapter;

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
        bookListings=new ArrayList<>();

        //todo Remove this
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("BookListings");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    BookListing bookListing = data.child("BookListing").getValue(BookListing.class);
                    System.out.println(bookListing.getOwnerUsername());
                    bookListings.add(bookListing);
                }
                fillLayout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        /*Book b1 = new Book("Title 1","Author 1","","1234567890");
        Book b2 = new Book("Title 2","Author 2","","1234567890");
        UserAccount u1 = new UserAccount("debug","debug");
        bookListings.add(new BookListing(b1,u1));
        bookListings.add(new BookListing(b2,u1));*/


        //bookListings = MockDatabase.getInstance().readAllBookListings();

        fillLayout();//todo delete when using real db
    }

    /**
     *
     */
    private void fillLayout(){
        //Setup RecyclerView
        searchResults = findViewById(R.id.searchResults);
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        listingAdapter = new BookListingAdapter(bookListings, this);
        searchResults.setAdapter(listingAdapter);
    }

}
