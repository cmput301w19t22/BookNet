package com.example.booknet;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    //The list of BookListings to display
    private Notifications data;

    //The activity this adapter was created from
    private AppCompatActivity sourceActivity;

    public NotificationAdapter(Notifications data, AppCompatActivity sourceActivity) {
        this.data = data;
        this.sourceActivity = sourceActivity;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Create a new view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notification_item_list, viewGroup, false);
        NotificationAdapter.NotificationViewHolder newNotificationViewHolder = new NotificationAdapter.NotificationViewHolder(view);
        return newNotificationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int position) {
        //Get the data at the provided position
        final BookListing item = data.getBookAtPosition(position);
        //Index to pass to the edit activity
        final int index = notificationViewHolder.getAdapterPosition();

        //Fill the text fields with the object's data
        //bookListingViewHolder.bookThumbnail.//todo apply photo
        notificationViewHolder.bookTitleLabel.setText(item.getBook().getTitle());
        notificationViewHolder.bookAuthorLabel.setText(item.getBook().getAuthor());
        notificationViewHolder.isbnLabel.setText(item.getBook().getIsbn());
        notificationViewHolder.ownerLabel.setText(item.getOwnerUsername());


        notificationViewHolder.statusLabel.setText(item.getStatusString());
        notificationViewHolder.item = item;

        //Add the click listener to the item
        /**
         * Click Listener for individual list items. Starts view activity for the clicked item.
         */
        notificationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedItem(item);
            }
        });
    }

    private void clickedItem(BookListing item) {
        //Start View/Edit Activity with Clicked Item
        Intent intent = new Intent(sourceActivity, ListingViewActivity.class);
        intent.putExtra("username", item.getOwnerUsername());
        intent.putExtra("bookisbn", item.getBook().getIsbn());
        sourceActivity.startActivity(intent);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        //Layout Objects
        private ImageView bookThumbnail;
        private TextView bookTitleLabel;
        private TextView bookAuthorLabel;
        private TextView isbnLabel;
        private TextView ownerLabel;
        private TextView statusLabel;
        private BookListing item;

        /**
         * Creates the BookListingViewHolder
         *
         * @param itemView The view for this item
         */
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            //Obtain Layout Object References
            bookThumbnail = itemView.findViewById(R.id.bookThumbnail);
            bookTitleLabel = itemView.findViewById(R.id.bookTitleLabel);
            bookAuthorLabel = itemView.findViewById(R.id.bookAuthorLabel);
            isbnLabel = itemView.findViewById(R.id.isbnLabel);
            ownerLabel = itemView.findViewById(R.id.ownerLabel);
            statusLabel = itemView.findViewById(R.id.statusLabel);
        }
    }
}
