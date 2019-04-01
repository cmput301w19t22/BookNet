package com.example.booknet.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.booknet.Adapters.RequestedLibraryAdapter;
import com.example.booknet.Adapters.SpaceDecoration;
import com.example.booknet.Constants.BookListingStatus;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.BookLibrary;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.CurrentUser;
import com.example.booknet.Model.Photo;
import com.example.booknet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An activity to display the requested library of a user. (ie. the books that user has requested)
 *
 * @author Jamie
 * @version 1.0
 */
public class RequestLibraryFragment extends Fragment {

    //Layout Objects
    private RecyclerView libraryListView;
    private RequestedLibraryAdapter listingAdapter;
    private ImageButton addButton;
    private TextView bookCountLabel;

    //Activity Data
    private BookLibrary filteredLibrary = new BookLibrary();

    DatabaseManager manager = DatabaseManager.getInstance();
    ValueEventListener listener;

    ReentrantReadWriteLock locks = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = locks.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = locks.writeLock();

    String selectedStatus = "All";

    public static RequestLibraryFragment newInstance() {
        RequestLibraryFragment myFragment = new RequestLibraryFragment();

        Bundle args = new Bundle();
//        args.putInt("someInt", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }

    /**
     * Called when creating the activity.
     *
     * @param savedInstanceState
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Create the view
        View view = inflater.inflate(R.layout.activity_owned_library, container, false);

        Log.d("seanTag", "onCreateView Request");

        bookCountLabel = view.findViewById(R.id.resultsNumLabel);

        //Deactivate Add Button
        addButton = view.findViewById(R.id.addBookButton);
        addButton.setVisibility(View.GONE);
        addButton.setClickable(false);

        TextView titlelabel = view.findViewById(R.id.titleLabel);
        titlelabel.setText("Requested Books");//Change the page title

        //Get Data From the Database
        writeLock.lock();
        filteredLibrary.copyOneByOne(manager.readUserRequestLibrary());
        writeLock.unlock();


        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // once the data is changed, we just change our corresponding static variable
                writeLock.lock();
                //first empty it
                filteredLibrary.removeAllBooks();
                Log.d("mattTag", "REMOVED");

                // then fill it as it is in the database
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    BookListing bookListing = data.getValue(BookListing.class);
                    if (bookListing != null) {
                        if (selectedStatus.equals("All")) {
                            if (bookListing.getRequests().contains(CurrentUser.getInstance().getUsername()) ||
                                    bookListing.getBorrowerName().equals(CurrentUser.getInstance().getUsername())) {
                                filteredLibrary.addBookListing(bookListing.clone());
                            }

                        } else if (selectedStatus.equals(bookListing.getStatus().toString())) {
                            if (bookListing.getRequests().contains(CurrentUser.getInstance().getUsername()) ||
                                    bookListing.getBorrowerName().equals(CurrentUser.getInstance().getUsername())) {
                                filteredLibrary.addBookListing(bookListing.clone());
                            }
                        }


                    }
                }

                listingAdapter.notifyDataSetChanged();
                bookCountLabel.setText(String.format("%d Book(s)",filteredLibrary.size()));
                writeLock.unlock();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };

        manager.getAllListingsRef().addValueEventListener(listener);


        //Setup RecyclerView
        libraryListView = view.findViewById(R.id.bookLibrary);
        libraryListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listingAdapter = new RequestedLibraryAdapter(filteredLibrary, readLock, getActivity());
        libraryListView.setAdapter(listingAdapter);
        libraryListView.addItemDecoration(new SpaceDecoration(12,16));

        //Setup Filter Menu todo

        Spinner filter = view.findViewById(R.id.filterSpinner);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView selectedView = (TextView) view;
                if (selectedView != null) {
                    selectedStatus = selectedView.getText().toString();
                    writeLock.lock();
                    if (selectedStatus.equals("All")) {
                        filteredLibrary.copyOneByOne(manager.readUserRequestLibrary());
                    } else {
                        filteredLibrary.filterByStatus(manager.readUserRequestLibrary(), BookListingStatus.valueOf(selectedStatus));
                    }
                    writeLock.unlock();
                    new ThumbnailFetchingTask(getActivity()).execute();
                    listingAdapter.notifyDataSetChanged();
                    bookCountLabel.setText(String.format("%d Book(s)",filteredLibrary.size()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new ThumbnailFetchingTask(getActivity()).execute();
        listingAdapter.notifyDataSetChanged();
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            manager.getAllListingsRef().removeEventListener(listener);//todo I get a null listener error here
        } catch (Exception e){
            //who knows
        }
    }


    public class ThumbnailFetchingTask extends AsyncTask<Void, Void, Boolean> {
        Activity context;

        ThumbnailFetchingTask(Activity context) {
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
                        Log.d("mattX", bl.toString() + " photo is not cached");
                        manager.fetchListingThumbnail(bl,
                                listingAdapter

                        );

                    } else {
                        Log.d("mattX", bl.toString() + " photo is cached in ownbooks");
                        bl.setPhoto(new Photo(thumbnailBitmap));
                        libraryListView.post(new Runnable()
                        {
                            @Override
                            public void run() {
                                listingAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

            }
            writeLock.unlock();


            return true;
        }


    }


}
