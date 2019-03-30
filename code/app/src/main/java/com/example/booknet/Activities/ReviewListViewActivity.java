package com.example.booknet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.Adapters.ReviewListAdapter;
import com.example.booknet.Adapters.SpaceDecoration;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.Review;
import com.example.booknet.Model.UserAccount;
import com.example.booknet.R;

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
    ImageButton backButton;

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
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //todo obtain user account from db
        String username = "";
        Intent intent = getIntent();
        if(intent.hasExtra("username")){
            username = intent.getStringExtra("username");
        }

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
        reviewList.addItemDecoration(new SpaceDecoration(12,16));

        reviewListAdapter.notifyDataSetChanged();
    }
}
