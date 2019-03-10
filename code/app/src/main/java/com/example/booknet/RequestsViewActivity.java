package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class RequestsViewActivity extends AppCompatActivity {

    //Layout Objects
    private RecyclerView requestsList;
    private UserRequestAdapter requestAdapter;

    //App Data
    BookListing listing;
    ArrayList<UserAccount> requests;

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
        if (intent.hasExtra("username") && intent.hasExtra("bookisbn")) {
            String username = intent.getStringExtra("username");
            String isbn = intent.getStringExtra("bookisbn");
            listing = MockDatabase.getInstance().readBookListing(username, isbn, MockDatabase.OWNEDLIBRARY);

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
            requests = listing.getRequesters();
        }

        //Add a fake request just for debugging
        requests.add(new UserAccount("ghostRequester", "debug"));

        //Setup RecyclerView
        requestsList = findViewById(R.id.requestList);
        requestsList.setLayoutManager(new LinearLayoutManager(this));
        requestAdapter = new UserRequestAdapter(listing, requests, this);
        requestsList.setAdapter(requestAdapter);
    }
}
