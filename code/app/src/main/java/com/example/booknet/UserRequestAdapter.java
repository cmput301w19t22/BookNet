package com.example.booknet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserRequestAdapter extends RecyclerView.Adapter<UserRequestAdapter.RequestViewHolder> {

    //The requester usernames
    private ArrayList<String> requesters = new ArrayList<>();

    //The listing the requests are for
    private BookListing listing;

    //The activity this adapter was created from
    private AppCompatActivity sourceActivity;
    private DatabaseManager manager = DatabaseManager.getInstance();

    //Image Drawables to use in this activity
    private int starOn = android.R.drawable.star_on;//todo replace with custom images
    private int starOff = android.R.drawable.star_off;


    /**
     * Creates the adapter
     *
     * @param listing        The UserAccounts to use for the list display
     * @param sourceActivity The activity that created this adapter
     */
    public UserRequestAdapter(BookListing listing, AppCompatActivity sourceActivity) {
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
        RequestViewHolder newMeasurementViewHolder = new RequestViewHolder(view);
        return newMeasurementViewHolder;
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
        float score = Float.parseFloat("2.0");
        requestViewHolder.ratingText.setText(String.format("%1.1f", score));
        requestViewHolder.star1.setImageResource((score >= 1) ? starOn : starOff);
        requestViewHolder.star2.setImageResource((score >= 2) ? starOn : starOff);
        requestViewHolder.star3.setImageResource((score >= 3) ? starOn : starOff);
        requestViewHolder.star4.setImageResource((score >= 4) ? starOn : starOff);
        requestViewHolder.star5.setImageResource((score >= 5) ? starOn : starOff);

        //Set Click Listeners
        // todo: fix this
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

//        MockDatabase.getInstance().acceptRequestForListing(listing, account);
        manager.acceptRequestForListing(listing, username);
        Toast.makeText(sourceActivity, "Accepted " + username, Toast.LENGTH_LONG).show();
        sourceActivity.finish();
    }

    /**
     * Declines the request from the account. Called when the decline button is pressed.
     *
     * @param account The user whose request will be declined.
     */
    private void declineButton(String account) {
        //todo deny the request in real db, I'll fix this - matt
        listing.denyRequest(account);
//        MockDatabase.getInstance().declineRequestForListing(listing, account);
        Toast.makeText(sourceActivity, "Declined " + account, Toast.LENGTH_LONG).show();
    }

    /**
     * Stores the view data for a list item.
     */
    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        //Layout Objects
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
