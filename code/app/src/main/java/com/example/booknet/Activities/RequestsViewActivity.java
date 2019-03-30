package com.example.booknet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.booknet.Adapters.RequestViewAdapter;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.BookListing;
import com.example.booknet.R;

import java.util.ArrayList;


/**
 * An activity for viewing which users have requested a listing and
 * allow the owner to acccept or deny each of these requests.
 *
 * @author Jamie
 */
public class RequestsViewActivity extends AppCompatActivity {

    //Layout Objects
    private RecyclerView requestsList;
    private RequestViewAdapter requestAdapter;
    private ImageButton backButton;
    private DatabaseManager manager = DatabaseManager.getInstance();

    //Activity Data
    BookListing listing;
    ArrayList<String> requests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_view);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
            int dupID = intent.getIntExtra("dupID", 0);

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
