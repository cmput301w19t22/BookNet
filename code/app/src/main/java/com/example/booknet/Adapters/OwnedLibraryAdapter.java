package com.example.booknet.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booknet.Activities.OwnListingViewActivity;
import com.example.booknet.Model.BookLibrary;
import com.example.booknet.Model.BookListing;
import com.example.booknet.R;

//Reused/adapted code from assignment 1

/**
 * A custom adapter to display BookListing objects in a RecyclerView.
 * Intended for viewing listings owned by the current user.
 *
 * @author Jamie
 * @version 1.0
 */
public class OwnedLibraryAdapter extends RecyclerView.Adapter<OwnedLibraryAdapter.OwnedListingViewHolder> {

    //The BookLibrary to display
    private BookLibrary library;

    //The activity this adapter was created from
    private FragmentActivity sourceActivity;

    /**
     * Creates the adapter
     *
     * @param library        The BookLibrary to use for the list display
     * @param sourceActivity The activity that created this adapter
     */
    public OwnedLibraryAdapter(BookLibrary library, FragmentActivity sourceActivity) {
        this.library = library;
        this.sourceActivity = sourceActivity;
    }

    /**
     * Routine when creating a new RequestListingViewHolder.
     * Assigns the list item layout to the new ViewHolder.
     *
     * @return A new RequestListingViewHolder using the list layout
     */
    @NonNull
    @Override
    public OwnedListingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Create a new view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.book_listing_item_list, viewGroup, false);
        OwnedListingViewHolder ownedListingViewHolder = new OwnedListingViewHolder(view);
        return ownedListingViewHolder;
    }

    /**
     * Routine for binding new library to a list item
     *
     * @param ownedListingViewHolder The ViewHolder to be assigned
     * @param position               Index in the list to use for this list slot
     */
    @Override
    public void onBindViewHolder(@NonNull OwnedListingViewHolder ownedListingViewHolder, int position) {
        //Get the library at the provided position
        final BookListing item = library.getBookAtPosition(position);
        //Index to pass to the edit activity
        final int index = ownedListingViewHolder.getAdapterPosition();

        //Fill the text fields with the object's library
        //ownedListingViewHolder.bookThumbnail.//todo listing photo
        ownedListingViewHolder.bookThumbnail.setImageResource(R.drawable.ic_book_default);
        ownedListingViewHolder.bookTitleLabel.setText(item.getBook().getTitle());
        ownedListingViewHolder.bookAuthorLabel.setText(item.getBook().getAuthor());
        ownedListingViewHolder.isbnLabel.setText(item.getBook().getIsbn());
        ownedListingViewHolder.ownerLabel.setVisibility(View.GONE);//Exclude this element
        ownedListingViewHolder.ownedLabel.setVisibility(View.GONE);//Exclude this element
        ownedListingViewHolder.statusLabel.setText(item.getStatus().toString());
        Log.d("mattTag", "really? " + item.getBook().toString() + " " + item.getStatus());

        /*if ((position & 1) == 1) {//check odd
            ownedListingViewHolder.constraintLayout.setBackgroundColor(sourceActivity.getResources().getColor(R.color.lightDarkerTint));
        }*/

        //Add the click listener to the item
        ownedListingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedItem(item);
            }
        });

    }

    /**
     * Click Listener for individual list items. Starts view activity for the clicked item.
     */
    public void clickedItem(BookListing item) {
        //Start View/Edit Activity with Clicked Item
        Intent intent = new Intent(sourceActivity, OwnListingViewActivity.class);
        if (item != null) {
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
        return library.size();
    }

    /**
     * Stores the view library for a list item.
     */
    public static class OwnedListingViewHolder extends RecyclerView.ViewHolder {

        //Layout Objects
        private ConstraintLayout constraintLayout;
        private ImageView bookThumbnail;
        private TextView bookTitleLabel;
        private TextView bookAuthorLabel;
        private TextView isbnLabel;
        private TextView ownerLabel;
        private TextView ownedLabel;
        private TextView statusLabel;


        /**
         * Creates the RequestListingViewHolder
         *
         * @param itemView The view for this item
         */
        public OwnedListingViewHolder(@NonNull View itemView) {
            super(itemView);

            //Obtain Layout Object References
            constraintLayout = itemView.findViewById(R.id.bookLayout);
            bookThumbnail = itemView.findViewById(R.id.bookThumbnail);
            bookTitleLabel = itemView.findViewById(R.id.bookTitleLabel);
            bookAuthorLabel = itemView.findViewById(R.id.bookAuthorLabel);
            isbnLabel = itemView.findViewById(R.id.isbnLabel);
            ownerLabel = itemView.findViewById(R.id.ownerLabel);
            ownedLabel = itemView.findViewById(R.id.ownedBy);
            statusLabel = itemView.findViewById(R.id.statusLabel);

            bookTitleLabel.setSelected(true);//select to enable scrolling
            bookAuthorLabel.setSelected(true);
            ownerLabel.setSelected(true);
        }
    }


}