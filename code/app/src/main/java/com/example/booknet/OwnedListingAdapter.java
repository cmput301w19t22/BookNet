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

//Reused/adapted code from assignment 1

/**
 * A custom adapter to display BookListing objects in a RecyclerView.
 * Intended for viewing listings owned by the current user.
 *
 * @author Jamie
 * @version 1.0
 */
public class OwnedListingAdapter extends RecyclerView.Adapter<OwnedListingAdapter.OwnedListingViewHolder> {

    //The BookLibrary to display
    private BookLibrary data;

    //The activity this adapter was created from
    private AppCompatActivity sourceActivity;

    /**
     * Creates the adapter
     *
     * @param data           The BookLibrary to use for the list display
     * @param sourceActivity The activity that created this adapter
     */
    public OwnedListingAdapter(BookLibrary data, AppCompatActivity sourceActivity) {
        this.data = data;
        this.sourceActivity = sourceActivity;
    }

    /**
     * Routine when creating a new OwnedListingViewHolder.
     * Assigns the list item layout to the new ViewHolder.
     *
     * @return A new OwnedListingViewHolder using the list layout
     */
    @NonNull
    @Override
    public OwnedListingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Create a new view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.book_listing_item_list, viewGroup, false);
        OwnedListingViewHolder newMeasurementViewHolder = new OwnedListingViewHolder(view);
        return newMeasurementViewHolder;
    }

    /**
     * Routine for binding new data to a list item
     *
     * @param ownedListingViewHolder The ViewHolder to be assigned
     * @param position              Index in the list to use for this list slot
     */
    @Override
    public void onBindViewHolder(@NonNull OwnedListingViewHolder ownedListingViewHolder, int position) {
        //Get the data at the provided position
        final BookListing item = data.getBooks().get(position);
        //Index to pass to the edit activity
        final int index = ownedListingViewHolder.getAdapterPosition();

        //Fill the text fields with the object's data
        //ownedListingViewHolder.bookThumbnail.
        ownedListingViewHolder.bookTitleLabel.setText(item.getBook().getTitle());
        ownedListingViewHolder.bookAuthorLabel.setText(item.getBook().getAuthor());
        ownedListingViewHolder.isbnLabel.setText(item.getBook().getIsbn());
        ownedListingViewHolder.ownerLabel.setVisibility(View.GONE);//Exclude this element
        ownedListingViewHolder.ownedLabel.setVisibility(View.GONE);//Exclude this element
        ownedListingViewHolder.statusLabel.setText(item.getStatus().toString());

        //Add the click listener to the item
        ownedListingViewHolder.itemView.setOnClickListener(bookListingListener);
    }

    /**
     * Click Listener for individual list items. Starts view activity for the clicked item.
     */
    View.OnClickListener bookListingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Start View/Edit Activity with Clicked Item
            Intent intent = new Intent(sourceActivity, OwnListingViewActivity.class);
            //intent.putExtra(ListingViewActivity, index);//Send listing object to activity
            //todo: intent
            sourceActivity.startActivity(intent);
        }
    };

    /**
     * Override to get the number of items in the dataset
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * Stores the view data for a list item.
     */
    public static class OwnedListingViewHolder extends RecyclerView.ViewHolder {

        //Layout Objects
        private ImageView bookThumbnail;
        private TextView bookTitleLabel;
        private TextView bookAuthorLabel;
        private TextView isbnLabel;
        private TextView ownerLabel;
        private TextView ownedLabel;
        private TextView statusLabel;


        /**
         * Creates the OwnedListingViewHolder
         *
         * @param itemView The view for this item
         */
        public OwnedListingViewHolder(@NonNull View itemView) {
            super(itemView);

            //Obtain Layout Object References
            bookThumbnail = itemView.findViewById(R.id.bookThumbnail);
            bookTitleLabel = itemView.findViewById(R.id.bookTitleLabel);
            bookAuthorLabel = itemView.findViewById(R.id.bookAuthorLabel);
            isbnLabel = itemView.findViewById(R.id.isbnLabel);
            ownerLabel = itemView.findViewById(R.id.ownerLabel);
            ownedLabel = itemView.findViewById(R.id.ownedLabel);
            statusLabel = itemView.findViewById(R.id.statusLabel);
        }
    }


}
