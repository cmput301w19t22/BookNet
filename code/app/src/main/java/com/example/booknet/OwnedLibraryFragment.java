package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.booknet.Constants.BookListingStatus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * An activity to display the owned library of a user.
 *
 * @author Jamie
 * @version 1.0
 */
public class OwnedLibraryFragment extends Fragment {

    //Layout Objects
    private RecyclerView libraryListView;
    private OwnedListingAdapter listingAdapter;
    private Button addButton;
    private ValueEventListener valueEventListener = null;

    //Activity Data
    private BookLibrary library;

    private ValueEventListener listener;
    private BookLibrary filteredLibrary = new BookLibrary();

    DatabaseManager manager = DatabaseManager.getInstance();

    public static OwnedLibraryFragment newInstance() {
        OwnedLibraryFragment myFragment = new OwnedLibraryFragment();

        Bundle args = new Bundle();
//        args.putInt("someInt", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }

    /**
     * Called when creating the activity.
     * Sets a click listener for the add button
     *
     * @param savedInstanceState
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_owned_library, container, false);

        filteredLibrary = new BookLibrary();
        //Add Click Listener
        addButton = view.findViewById(R.id.addBookButton);
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

        manager.getUserListingsRef().addValueEventListener(listener);

        filteredLibrary = library.clone();


        Log.d("matt", "creating new adpator");

        libraryListView = view.findViewById(R.id.bookLibrary);
        libraryListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listingAdapter = new OwnedListingAdapter(filteredLibrary, getActivity());
        libraryListView.setAdapter(listingAdapter);

        Spinner filter = view.findViewById(R.id.spinner);

        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView selectedView = (TextView) view;
                if (selectedView != null){
                    String selectedItem = selectedView.getText().toString();
                    if (selectedItem.equals("All")) {
                        Log.d("mattTag", "copying one by one");
                        filteredLibrary.copyOneByOne(library);
                        Log.d("mattTag", "after copying: " + filteredLibrary.toString());
                    } else {
                        Log.d("mattTag", "yi");
                        filteredLibrary.filterByStatus(library, BookListingStatus.valueOf(selectedItem));
                    }

                    listingAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    /**
     * Called when the activity starts
     * Tells the list's adapter to update.
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d("mattTag", "starting the activity, notifying");
        Log.d("mattTag", "when starting, the books are: " + filteredLibrary.toString());
        //Update List Data
        listingAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        manager.getUserListingsRef().removeEventListener(listener);
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
        Intent intent = new Intent(getActivity(), NewBookActivity.class);
        startActivity(intent);
    }
}
