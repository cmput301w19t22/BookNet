package com.example.booknet.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booknet.Activities.ListingViewActivity;
import com.example.booknet.Model.BookLibrary;
import com.example.booknet.Model.BookListing;
import com.example.booknet.R;

//Reused/adapted code from assignment 1

/**
 * A custom adapter to display BookListing objects in a RecyclerView.
 * Intended for viewing listings owned by other users.
 *
 * @author Jamie
 * @version 1.0
 */
public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.BookListingViewHolder> {

    //The list of BookListings to display
    private BookLibrary data;
    private LayoutInflater inflater;

    //The activity this adapter was created from
    private FragmentActivity sourceActivity;


    /**
     * Creates the adapter
     *
     * @param data           The list of BookListings to use for the list display
     * @param sourceActivity The activity that created this adapter
     */
    public BookSearchAdapter(BookLibrary data, FragmentActivity sourceActivity) {
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
        BookListingViewHolder bookListingViewHolder = new BookListingViewHolder(view);
        return bookListingViewHolder;
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
        final BookListing item = data.getBookAtPosition(position);
        //Index to pass to the edit activity
        final int index = bookListingViewHolder.getAdapterPosition();

        //Fill the text fields with the object's data
        //bookListingViewHolder.bookThumbnail.//todo apply photo
        bookListingViewHolder.bookThumbnail.setImageResource(R.drawable.ic_photo_lightgray_24dp);
        bookListingViewHolder.bookTitleLabel.setText(item.getBook().getTitle());
        bookListingViewHolder.bookAuthorLabel.setText(item.getBook().getAuthor());
        bookListingViewHolder.isbnLabel.setText(item.getBook().getIsbn());
        bookListingViewHolder.ownerLabel.setText(item.getOwnerUsername());
        bookListingViewHolder.statusLabel.setText(item.getStatus().toString());
        bookListingViewHolder.item = item;

        if ((position & 1) == 1) {//check odd
            bookListingViewHolder.constraintLayout.setBackgroundColor(sourceActivity.getResources().getColor(R.color.lightDarkerTint));
        }

        //Add the click listener to the item
        /**
         * Click Listener for individual list items. Starts view activity for the clicked item.
         */
        bookListingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedItem(item);
            }
        });
    }

    /**
     * To be called when an item is clicked. Starts a view activity for the clicked Booklisting
     *
     * @param item The BookListing we clicked on
     */
    private void clickedItem(BookListing item) {
        //Start View/Edit Activity with Clicked Item
        Intent intent = new Intent(sourceActivity, ListingViewActivity.class);
        if (item != null) {
            intent.putExtra("ownerUsername", item.getOwnerUsername());
            intent.putExtra("isbn", item.getBook().getIsbn());
            intent.putExtra("dupID", item.getDupInd());
        }
        sourceActivity.startActivity(intent);
    }

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
        private ConstraintLayout constraintLayout;
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
        public BookListingViewHolder(@NonNull View itemView) {
            super(itemView);

            //Obtain Layout Object References
            constraintLayout = itemView.findViewById(R.id.bookLayout);
            bookThumbnail = itemView.findViewById(R.id.bookThumbnail);
            bookTitleLabel = itemView.findViewById(R.id.bookTitleLabel);
            bookAuthorLabel = itemView.findViewById(R.id.bookAuthorLabel);
            isbnLabel = itemView.findViewById(R.id.isbnLabel);
            ownerLabel = itemView.findViewById(R.id.ownerLabel);
            statusLabel = itemView.findViewById(R.id.statusLabel);

            bookTitleLabel.setSelected(true);//select to enable scrolling
            bookAuthorLabel.setSelected(true);
            ownerLabel.setSelected(true);
        }
    }


}