package com.example.booknet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
public class BookSearchFragment extends Fragment {

    //Layout Objects
    private RecyclerView searchResults;
    private ProgressBar progressBar;
    private BookListingAdapter listingAdapter;
    private DatabaseManager manager = DatabaseManager.getInstance();

    //App Data
    BookLibrary allBookListings;

    private BookLibrary filteredLibrary = new BookLibrary();
    private ValueEventListener listener;

    SearchView searchBar;

    public static BookSearchFragment newInstance() {
        BookSearchFragment myFragment = new BookSearchFragment();

        Bundle args = new Bundle();
//        args.putInt("someInt", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_book_search, container, false);
        searchResults = view.findViewById(R.id.searchResults);
        searchBar = view.findViewById(R.id.searchBar);

        allBookListings = manager.readAllBookListings();
        filteredLibrary.copyOneByOne(allBookListings);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // once the data is changed, we just change our corresponding static variable

                //first empty it
                if (searchBar != null) {
                    filteredLibrary.removeAllBooks();

                    // then fill it as it is in the database
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        BookListing bookListing = data.getValue(BookListing.class);
                        if (bookListing != null) {
                            if (bookListing.containKeyword(searchBar.getQuery().toString())) {
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

        manager.getAllListingsRef().addValueEventListener(listener);

//        mSoundPool = new SoundPool(MAX_STREAM, AudioManager.STREAM_MUSIC, 0);
//        final int backgroundSoundId = mSoundPool.load(this, R.raw.nice_keyboard_sound, 0);

//        player.prepareAsync();

        allBookListings = manager.readAllBookListings();

        searchResults.setLayoutManager(new LinearLayoutManager(getActivity()));
        listingAdapter = new BookListingAdapter(filteredLibrary, getActivity());
        searchResults.setAdapter(listingAdapter);

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
                for (BookListing listing : allBookListings) {
                    if (listing.containKeyword(s)) {
                        filteredLibrary.addBookListing(listing.clone());
                    }

                }
                listingAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //listingAdapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        //listingAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        manager.getAllListingsRef().removeEventListener(listener);
        super.onDestroy();
    }
}
