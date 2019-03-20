package com.example.booknet;

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

/**
 * An activity to display the requested library of a user. (ie. the books that user has requested)
 *
 * @author Jamie
 * @version 1.0
 */
public class RequestLibraryFragment extends Fragment {

    //Layout Objects
    private RecyclerView libraryListView;
    private OwnedLibraryAdapter listingAdapter;
    private Button addButton;

    //Activity Data
    private BookLibrary library = new BookLibrary();
    private BookLibrary filteredLibrary;

    DatabaseManager manager = DatabaseManager.getInstance();


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
        //(Currently reusing the ui from the owned library layout) todo? use it's own layout?
        View view = inflater.inflate(R.layout.activity_owned_library, container, false);

        Log.d("seanTag", "onCreateView Request");

        //Add Click Listener
        addButton = view.findViewById(R.id.addBookButton);
        addButton.setVisibility(View.GONE);//Dont use this button

        TextView debuglabel = view.findViewById(R.id.debugLabel);//Change the debug label todo remove later
        debuglabel.setText("Requested Books");

        //Get Data From the Database
        library = manager.readUserRequestLibrary();
        //Put a null check while db functions being worked on
        if (library == null) {
            library = new BookLibrary();
        }
        filteredLibrary = library.clone();

        //Setup RecyclerView
        libraryListView = view.findViewById(R.id.bookLibrary);
        libraryListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listingAdapter = new OwnedLibraryAdapter(library, getActivity());
        libraryListView.setAdapter(listingAdapter);

        //Setup Filter Menu todo
        Spinner filter = view.findViewById(R.id.spinner);

        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView selectedView = (TextView) view;
                if (selectedView != null) {
                    String selectedItem = selectedView.getText().toString();
                    if (selectedItem.equals("All")) {
                        filteredLibrary.copyOneByOne(library);
                    } else {
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

}
