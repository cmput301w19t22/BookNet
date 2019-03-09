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

import java.util.ArrayList;

//Reused/adapted code from assignment 1

/**
 * A custom adapter to display BookListing objects in a RecyclerView.
 * Intended for viewing listings owned by other users.
 *
 * @author Jamie
 * @version 1.0
 */
public class BookListingAdapter extends RecyclerView.Adapter<BookListingAdapter.BookListingViewHolder> {

    //The list of BookListings to display
    private ArrayList<BookListing> data;


    //The activity this adapter was created from
    private AppCompatActivity sourceActivity;

    /**
     * Creates the adapter
     *
     * @param data           The list of BookListings to use for the list display
     * @param sourceActivity The activity that created this adapter
     */
    public BookListingAdapter(ArrayList<BookListing> data, AppCompatActivity sourceActivity) {
        this.data = data;
        this.sourceActivity = sourceActivity;
    }

    /**
     * Routine when creating a new BookListingViewHolder.
     * Assigns the list item layout to the new ViewHolder.
     *
     * @return A new BookListingViewHolder using the list layout
     */
    @NonNull
    @Override
    public BookListingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Create a new view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.book_listing_item_list, viewGroup, false);
        BookListingViewHolder newMeasurementViewHolder = new BookListingViewHolder(view);
        return newMeasurementViewHolder;
    }

    /**
     * Routine for binding new data to a list item
     *
     * @param bookListingViewHolder The ViewHolder to be assigned
     * @param position              Index in the list to use for this list slot
     */
    @Override
    public void onBindViewHolder(@NonNull BookListingViewHolder bookListingViewHolder, int position) {
        //Get the data at the provided position
        final BookListing item = data.get(position);
        //Index to pass to the edit activity
        final int index = bookListingViewHolder.getAdapterPosition();

        //Fill the text fields with the object's data
        //bookListingViewHolder.bookThumbnail.
        bookListingViewHolder.bookTitleLabel.setText(item.getBook().getTitle());
        bookListingViewHolder.bookAuthorLabel.setText(item.getBook().getAuthor());
        bookListingViewHolder.isbnLabel.setText(item.getBook().getIsbn());
        bookListingViewHolder.ownerLabel.setText(item.getOwnerName().getUsername());
        bookListingViewHolder.statusLabel.setText(item.getStatus().toString());

        //Add the click listener to the item
        bookListingViewHolder.itemView.setOnClickListener(bookListingListener);
    }

    /**
     * Click Listener for individual list items. Starts view activity for the clicked item.
     */
    View.OnClickListener bookListingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Start View/Edit Activity with Clicked Item
            Intent intent = new Intent(sourceActivity, ListingViewActivity.class);
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
    public static class BookListingViewHolder extends RecyclerView.ViewHolder {

        //Layout Objects
        private ImageView bookThumbnail;
        private TextView bookTitleLabel;
        private TextView bookAuthorLabel;
        private TextView isbnLabel;
        private TextView ownerLabel;
        private TextView statusLabel;

        /**
         * Creates the BookListingViewHolder
         *
         * @param itemView The view for this item
         */
        public BookListingViewHolder(@NonNull View itemView) {
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
