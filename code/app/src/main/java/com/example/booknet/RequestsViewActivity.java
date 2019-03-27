package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * An activity for viewing which users have requested a listing and
 * allow the owner to acccept or deny each of these requests.
 * @author Jamie
 */
public class RequestsViewActivity extends AppCompatActivity {

    //Layout Objects
    private RecyclerView requestsList;
    private RequestViewAdapter requestAdapter;
    private DatabaseManager manager = DatabaseManager.getInstance();

    //App Data
    BookListing listing;
    ArrayList<String> requests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_view);

        //todo get real data
        //Get Intent
        Intent intent = getIntent();
        if (intent.hasExtra("listing")) {
            //listing = (BookListing) intent.getSerializableExtra("listing");
        }
        //Check if given info to fetch listing
        if (intent.hasExtra("username") && intent.hasExtra("bookisbn") && intent.hasExtra("dupID")) {
            String username = intent.getStringExtra("username");
            String isbn = intent.getStringExtra("bookisbn");
            int  dupID = intent.getIntExtra("dupID", 0);

            listing = manager.readBookListingOfUsername(username, isbn, dupID);

        }
        fillLayout();

    }

    /**
     * Fills the layout with the data in the listing
     */
    private void fillLayout() {
        if (listing == null) {
            Toast.makeText(this, "Listing Not Found", Toast.LENGTH_LONG).show();
        } else {
            requests = listing.getRequests();
        }

        //Setup RecyclerView
        requestsList = findViewById(R.id.requestList);
        requestsList.setLayoutManager(new LinearLayoutManager(this));
        requestAdapter = new RequestViewAdapter(listing, this);
        requestsList.setAdapter(requestAdapter);

        requestAdapter.notifyDataSetChanged();
    }
}
