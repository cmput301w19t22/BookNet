package com.example.booknet.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.DatabaseManager;
import com.example.booknet.Fragments.UserProfileViewFragment;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.Review;
import com.example.booknet.R;

import java.util.ArrayList;


/**
 * Adapter for displaying a request in a recycler view list with accept and decline buttons.
 *
 * @author Jamie
 */
public class RequestViewAdapter extends RecyclerView.Adapter<RequestViewAdapter.RequestViewHolder> {

    //The requester usernames
    private ArrayList<String> requesters = new ArrayList<>();

    //The listing the requests are for
    private BookListing listing;

    //The activity this adapter was created from
    private AppCompatActivity sourceActivity;
    private DatabaseManager manager = DatabaseManager.getInstance();

    //Image Drawables to use in this activity
    private int starOn = R.drawable.ic_star_24dp;
    private int starOff = R.drawable.ic_star_border_24dp;
    private int starHalf = R.drawable.ic_star_half_24dp;


    /**
     * Creates the adapter
     *
     * @param listing        The UserAccounts to use for the list display
     * @param sourceActivity The activity that created this adapter
     */
    public RequestViewAdapter(BookListing listing, AppCompatActivity sourceActivity) {
        this.listing = listing;
        this.requesters = listing.getRequests();

        this.sourceActivity = sourceActivity;
    }

    /**
     * Routine when creating a new RequestViewHolder.
     * Assigns the list item layout to the new ViewHolder.
     *
     * @return A new RequestViewHolder using the list layout
     */
    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Create a new view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_request_item_list, viewGroup, false);
        RequestViewHolder requestViewHolder = new RequestViewHolder(view);
        return requestViewHolder;
    }

    /**
     * Routine for binding new data to a list item
     *
     * @param requestViewHolder The ViewHolder to be assigned
     * @param position          Index in the list to use for this list slot
     */
    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder requestViewHolder, int position) {
        //Get the data at the provided position
        final String username = requesters.get(position);
        //Index to pass to the edit activity
        final int index = requestViewHolder.getAdapterPosition();

        //Fill the text fields with the object's data
        requestViewHolder.username.setText(username);
        // todo: use real score
        float score = manager.readUserReviewAverage(username);
        int[] stars = new int[]{starOff, starHalf, starOn};
        if (score >= 0) {
            requestViewHolder.ratingText.setText(String.format("%1.1f", score));
        } else {
            requestViewHolder.ratingText.setText("---");
        }
        requestViewHolder.star1.setImageResource(Review.starImage(score, 0, stars));
        requestViewHolder.star2.setImageResource(Review.starImage(score, 1, stars));
        requestViewHolder.star3.setImageResource(Review.starImage(score, 2, stars));
        requestViewHolder.star4.setImageResource(Review.starImage(score, 3, stars));
        requestViewHolder.star5.setImageResource(Review.starImage(score, 4, stars));

        /*if ((position & 1) == 1) {//check odd
            requestViewHolder.constraintLayout.setBackgroundColor(sourceActivity.getResources().getColor(R.color.lightDarkerTint));
        }*/

        //Set Click Listeners
        requestViewHolder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile(username);
            }
        });

        requestViewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptButton(username);
            }
        });
        requestViewHolder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineButton(username);
            }
        });

        TranslateAnimation animIn = new TranslateAnimation(0.0f, 0f, 2000f, 0f);
        animIn.setDuration(500);
        requestViewHolder.itemView.startAnimation(animIn);
    }

    /**
     * Override to get the number of items in the dataset
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return requesters.size();
    }


    /**
     * Action to view the profile of the clicked user.
     */
    private void goToProfile(String username) {
        Intent intent = new Intent(sourceActivity, UserProfileViewFragment.class);
        if (username != null) {
            intent.putExtra("username", username);
        }
        sourceActivity.startActivity(intent);
    }

    /**
     * Accepts the request from the account. Called when the accept button is pressed.
     *
     * @param username The user whose request will be accepted.
     */
    private void acceptButton(String username) {
        manager.acceptRequestForListing(listing, username);
        Toast.makeText(sourceActivity, "Accepted " + username, Toast.LENGTH_LONG).show();
        sourceActivity.finish();
    }

    /**
     * Declines the request from the account. Called when the decline button is pressed.
     *
     * @param username The user whose request will be declined.
     */
    private void declineButton(String username) {
        //listing.denyRequest(account);
        manager.declineRequestForListing(listing, username);
        Toast.makeText(sourceActivity, "Declined " + username, Toast.LENGTH_LONG).show();
    }

    /**
     * Stores the view data for a list item.
     */
    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        //Layout Objects
        private ConstraintLayout constraintLayout;
        private TextView username;
        private TextView ratingText;
        private ImageView star1;
        private ImageView star2;
        private ImageView star3;
        private ImageView star4;
        private ImageView star5;

        private Button acceptButton;
        private Button declineButton;


        /**
         * Creates the RequestViewHolder
         *
         * @param itemView The view for this item
         */
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            //Obtain Layout Object References
            constraintLayout = itemView.findViewById(R.id.requestLayout);
            username = itemView.findViewById(R.id.userNameLabel);
            ratingText = itemView.findViewById(R.id.ratingTextLabel);
            star1 = itemView.findViewById(R.id.ratingStar1);
            star2 = itemView.findViewById(R.id.ratingStar2);
            star3 = itemView.findViewById(R.id.ratingStar3);
            star4 = itemView.findViewById(R.id.ratingStar4);
            star5 = itemView.findViewById(R.id.ratingStar5);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }

}
