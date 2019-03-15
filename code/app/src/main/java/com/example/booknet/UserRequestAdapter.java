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

    //The requesters to display
    ArrayList<UserAccount> requesters;

    //The listing the requests are for
    BookListing listing;

    //The activity this adapter was created from
    private AppCompatActivity sourceActivity;


    /**
     * Creates the adapter
     *
     * @param listing        The UserAccounts to use for the list display
     * @param sourceActivity The activity that created this adapter
     */
    public UserRequestAdapter(BookListing listing, AppCompatActivity sourceActivity) {
        this.listing = listing;
        //this.requesters = listing.getRequesters();
        getUserAccounts();
        this.sourceActivity = sourceActivity;
    }

    /**
     * Creates the adapter
     *
     * @param listing        The UserAccounts to use for the list display
     * @param sourceActivity The activity that created this adapter
     */
    public UserRequestAdapter(BookListing listing, ArrayList<UserAccount> requesters, AppCompatActivity sourceActivity) {
        this.listing = listing;
        this.requesters = requesters;
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
        final UserAccount account = requesters.get(position);
        final String username = account.getUsername();
        //Index to pass to the edit activity
        final int index = requestViewHolder.getAdapterPosition();

        //Fill the text fields with the object's data
        requestViewHolder.username.setText(username);
        requestViewHolder.ratingText.setText(String.format("%1.1f", account.getRatingScore()));
        //todo apply to stars

        //Set Click Listeners
        requestViewHolder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile();
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
     * Copies the BookListing's requesters into an array of UserAccounts
     */
    // todo: mockdatabase deprecated. I'll fix this - Matt
    private void getUserAccounts() {
        for (String username : listing.getRequesters()) {
            //Obtain the user from the database
            UserAccount requester = MockDatabase.getInstance().readUserAccount(username);
            if (requester != null) {
                requesters.add(requester);
            }
        }
    }


    /**
     * Action to view the profile of the clicked user.
     */
    private void goToProfile() {
        Intent intent = new Intent(sourceActivity, UserProfileViewActivity.class);
        sourceActivity.startActivity(intent);
    }

    /**
     * Accepts the request from the account. Called when the accept button is pressed.
     *
     * @param account The user whose request will be accepted.
     */
    private void acceptButton(String account) {
        //todo accept the request in real db. I'll fixe this -matt
        listing.acceptRequest(account);
//        MockDatabase.getInstance().acceptRequestForListing(listing, account);
        Toast.makeText(sourceActivity, "Accepted " + account, Toast.LENGTH_LONG).show();
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
