package com.example.booknet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booknet.Adapters.ReviewListAdapter;
import com.example.booknet.Adapters.SpaceDecoration;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.Review;
import com.example.booknet.Model.ReviewList;
import com.example.booknet.R;

/**
 * Activity to view a user's reviews.
 *
 * @author Jamie
 */
public class ReviewListViewActivity extends AppCompatActivity {

    //Layout Objects
    private RecyclerView reviewList;
    private ReviewListAdapter reviewListAdapter;
    private TextView usernameLabel;
    private TextView ratingAverageLabel;
    private TextView ratingCountLabel;
    private ImageButton backButton;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;

    //Image Drawables to use in this activity
    private int starOn = R.drawable.ic_star_24dp;
    private int starOff = R.drawable.ic_star_border_24dp;
    private int starHalf = R.drawable.ic_star_half_24dp;

    //Activity Data
    //UserAccount user;
    ReviewList reviews = new ReviewList();
    private String username;

    private DatabaseManager manager = DatabaseManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list_view);

        reviewList = findViewById(R.id.reviewList);
        usernameLabel = findViewById(R.id.usernameLabel);
        backButton = findViewById(R.id.backButton);
        ratingAverageLabel = findViewById(R.id.ratingTextLabel);
        ratingCountLabel = findViewById(R.id.numReviews);
        star1 = findViewById(R.id.ratingStar1);
        star2 = findViewById(R.id.ratingStar2);
        star3 = findViewById(R.id.ratingStar3);
        star4 = findViewById(R.id.ratingStar4);
        star5 = findViewById(R.id.ratingStar5);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("username")) {
            username = intent.getStringExtra("username");
        }

        fillLayout();
    }


    /**
     * Fills the layout with the data in the listing
     */
    private void fillLayout() {
        reviews = manager.readReviews(username);

        //Fill Summary
        usernameLabel.setText(username);
        float ratingTotal = 0;
        for (Review review : reviews) {
            ratingTotal += review.getScore();
        }
        float ratingAverage = (reviews.size() > 0) ? ratingTotal / reviews.size() : -1;
        if (ratingAverage >= 0) {
            ratingAverageLabel.setText(String.format("%1.1f", ratingAverage));
        }else {
            ratingAverageLabel.setText("---");
        }
        int[] stars = new int[]{starOff,starHalf,starOn};
        star1.setImageResource(Review.starImage(ratingAverage,0,stars));
        star2.setImageResource(Review.starImage(ratingAverage,1,stars));
        star3.setImageResource(Review.starImage(ratingAverage,2,stars));
        star4.setImageResource(Review.starImage(ratingAverage,3,stars));
        star5.setImageResource(Review.starImage(ratingAverage,4,stars));

        ratingCountLabel.setText(String.format("(%d Ratings)", reviews.size()));

        //Setup RecyclerView
        reviewList.setLayoutManager(new LinearLayoutManager(this));
        reviewListAdapter = new ReviewListAdapter(reviews, this);
        reviewList.setAdapter(reviewListAdapter);
        reviewList.addItemDecoration(new SpaceDecoration(12, 16));

        reviewListAdapter.notifyDataSetChanged();
    }
}
