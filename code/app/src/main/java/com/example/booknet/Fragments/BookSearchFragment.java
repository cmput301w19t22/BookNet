package com.example.booknet.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.booknet.Adapters.BookSearchAdapter;
import com.example.booknet.Adapters.SpaceDecoration;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.BookLibrary;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.Photo;
import com.example.booknet.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    private BookSearchAdapter listingAdapter;
    private SearchView searchBar;
    private Spinner filter;
    private DatabaseManager manager = DatabaseManager.getInstance();

    //App Data
    BookLibrary allBookListings;

    private BookLibrary filteredLibrary = new BookLibrary();
    private ValueEventListener listener;

    ReentrantReadWriteLock locks = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = locks.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = locks.writeLock();


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

        //Get Layout Objects
        searchResults = view.findViewById(R.id.searchResults);
        searchBar = view.findViewById(R.id.searchBar);
        filter = view.findViewById(R.id.searchFilter);

        allBookListings = manager.readAllBookListings();
        filteredLibrary.copyOneByOne(allBookListings);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // once the data is changed, we just change our corresponding static variable

                //first empty it
                if (searchBar != null) {

                    writeLock.lock();
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
                    writeLock.unlock();
                    new ThumnailFetchingTask(getActivity()).execute();
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        searchResults.setLayoutManager(layoutManager);
        listingAdapter = new BookSearchAdapter(filteredLibrary, getActivity(), readLock);
        searchResults.setAdapter(listingAdapter);
        searchResults.addItemDecoration(new SpaceDecoration(12,16));

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // async sound play needs to use the soundpool thing
//                mSoundPool.play(backgroundSoundId, (float)1, (float)1, 1, 0, (float)1);
                writeLock.lock();
                filteredLibrary.removeAllBooks();
                for (BookListing listing : allBookListings) {
                    if (listing.containKeyword(s)) {
                        filteredLibrary.addBookListing(listing.clone());
                    }

                }
                writeLock.unlock();
                new ThumnailFetchingTask(getActivity()).execute();
                listingAdapter.notifyDataSetChanged();
                return true;
            }
        });

        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //todo filter results
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        new ThumnailFetchingTask(getActivity()).execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Reset the filter
        filter.setSelection(0);
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
        super.onDestroy();
        manager.getAllListingsRef().removeEventListener(listener);
    }

    public class ThumnailFetchingTask extends AsyncTask<Void, Void, Boolean> {
        Activity context;

        ThumnailFetchingTask(Activity context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            writeLock.lock();
            for (final BookListing bl : filteredLibrary) {
                if (bl.getPhotoBitmap() == null) {

                    Log.d("mattX", bl.toString() + " photo is null");

                    Bitmap thumbnailBitmap = manager.getCachedThumbnail(bl);

                    if (thumbnailBitmap == null) {
                        manager.fetchListingThumbnail(bl,
                                listingAdapter,

                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("imageFetching", "fetching failed, cause: " + e.getLocalizedMessage());
                                    }
                                });

                    } else {
                        bl.setPhoto(new Photo(thumbnailBitmap));
                    }
                }

            }
            writeLock.unlock();


            return true;
        }


    }

}
