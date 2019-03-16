package com.example.booknet;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseManager manager = DatabaseManager.getInstance();

//    private SoundPool mSoundPool;
//    private final int MAX_STREAM = 10;
//    private boolean soundLoaded = false;

    //App Data
    BookLibrary allBookListings;

    private BookLibrary filteredLibrary = new BookLibrary();
    private ValueEventListener listener;

    SearchView searchBar;




    /**
     * Called when the activity is created.
     * Sets up the search results list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allBookListings = manager.readAllBookListings();
        filteredLibrary.copyOneByOne(allBookListings);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // once the data is changed, we just change our corresponding static variable

                //first empty it
                if (searchBar != null){
                    filteredLibrary.removeAllBooks();

                    // then fill it as it is in the database
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        BookListing bookListing = data.getValue(BookListing.class);
                        if (bookListing != null) {
                            if (bookListing.containKeyword(searchBar.getQuery().toString())){
                                filteredLibrary.addBookListing(bookListing.clone());
                            }

                        }
                    }
                    listingAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        manager.getAllLisitngsRef().addValueEventListener(listener);

//        mSoundPool = new SoundPool(MAX_STREAM, AudioManager.STREAM_MUSIC, 0);
//        final int backgroundSoundId = mSoundPool.load(this, R.raw.nice_keyboard_sound, 0);

//        player.prepareAsync();
        setContentView(R.layout.activity_book_search);



        allBookListings = manager.readAllBookListings();


        searchResults = findViewById(R.id.searchResults);
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        listingAdapter = new BookListingAdapter(filteredLibrary, this);
        searchResults.setAdapter(listingAdapter);

        SearchView searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // async sound play needs to use the soundpool thing
//                mSoundPool.play(backgroundSoundId, (float)1, (float)1, 1, 0, (float)1);


                filteredLibrary.removeAllBooks();
                for (BookListing listing: allBookListings){
                    if (listing.containKeyword(s)){
                        filteredLibrary.addBookListing(listing.clone());
                    }

                }
                listingAdapter.notifyDataSetChanged();



                return true;
            }
        });
    }


    public void onDestroy() {
        manager.getAllLisitngsRef().removeEventListener(listener);
        super.onDestroy();
    }


}
