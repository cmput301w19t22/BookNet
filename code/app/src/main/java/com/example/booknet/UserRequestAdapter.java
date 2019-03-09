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

import java.util.ArrayList;

public class UserRequestAdapter extends RecyclerView.Adapter<UserRequestAdapter.RequestViewHolder> {

    //The requesters to display
    ArrayList<UserAccount> requesters;

    //The activity this adapter was created from
    private AppCompatActivity sourceActivity;


    /**
     * Creates the adapter
     *
     * @param requesters     The UserAccounts to use for the list display
     * @param sourceActivity The activity that created this adapter
     */
    public UserRequestAdapter(ArrayList<UserAccount> requesters, AppCompatActivity sourceActivity) {
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
        final UserAccount item = requesters.get(position);
        //Index to pass to the edit activity
        final int index = requestViewHolder.getAdapterPosition();

        //Fill the text fields with the object's data
        requestViewHolder.username.setText(item.getUsername());
        requestViewHolder.ratingText.setText(String.format("%1.1f", item.getRatingScore()));
        //todo apply to stars

        requestViewHolder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile();
            }
        });

        requestViewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo accept the request
            }
        });
        requestViewHolder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo deny the request
            }
        });
    }

    @Override
    public int getItemCount() {
        return requesters.size();
    }

    /**
     * Action to view the profile of the clicked user.
     */
    public void goToProfile() {
        Intent intent = new Intent(sourceActivity, UserProfileViewActivity.class);
        sourceActivity.startActivity(intent);
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
