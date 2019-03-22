package com.example.booknet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Activity to view a user's reviews.
 *
 * @author Jamie
 */
public class ReviewListViewActivity extends AppCompatActivity {

    //Layout Objects
    RecyclerView reviewList;
    ReviewListAdapter reviewListAdapter;
    TextView usernameLabel;

    //Activity Data
    UserAccount user;
    ArrayList<Review> reviews = new ArrayList<>();
    private DatabaseManager manager = DatabaseManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list_view);

        reviewList = findViewById(R.id.reviewList);
        usernameLabel = findViewById(R.id.usernameLabel);

        //todo obtain user account from db

        fillLayout();
    }


    /**
     * Fills the layout with the data in the listing
     */
    private void fillLayout() {
        if (user == null) {
            Toast.makeText(this, "User Could Not Be Loaded", Toast.LENGTH_LONG).show();
        } else {
            reviews = user.getReviews();
        }

        if (user != null) {
            //Fill Summary
            usernameLabel.setText(user.getUsername());
        }

        //Setup RecyclerView
        reviewList.setLayoutManager(new LinearLayoutManager(this));
        reviewListAdapter = new ReviewListAdapter(reviews, this);
        reviewList.setAdapter(reviewListAdapter);

        reviewListAdapter.notifyDataSetChanged();
    }
}
