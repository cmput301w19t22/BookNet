package com.example.booknet.Adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booknet.Activities.ListingViewActivity;
import com.example.booknet.Model.BookLibrary;
import com.example.booknet.Model.BookListing;
import com.example.booknet.R;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//Reused/adapted code from assignment 1

/**
 * A custom adapter to display BookListing objects in a RecyclerView.
 * Intended for viewing listings owned by the current user.
 *
 * @author Jamie
 * @version 1.0
 */
public class RequestedLibraryAdapter extends RecyclerView.Adapter<RequestedLibraryAdapter.RequestListingViewHolder> {

    //The BookLibrary to display
    private BookLibrary library;

    //The activity this adapter was created from
    private FragmentActivity sourceActivity;

    private ReentrantReadWriteLock.ReadLock readLock;

    private ArrayList<RequestListingViewHolder> viewHolders = new ArrayList<>();

    /**
     * Creates the adapter
     *
     * @param library        The BookLibrary to use for the list display
     * @param sourceActivity The activity that created this adapter
     */
    public RequestedLibraryAdapter(BookLibrary library, ReentrantReadWriteLock.ReadLock readLock, FragmentActivity sourceActivity) {
        this.library = library;
        this.sourceActivity = sourceActivity;
        this.readLock = readLock;

        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                for (RequestListingViewHolder holder : viewHolders) {
                    if (holder != null) {
                        Log.d("jamie", "disable animation for " + holder.toString());
                        holder.allowAnimation = false;
                    } else {
                        viewHolders.remove(holder);
                    }
                }
            }
        });
    }

    /**
     * Routine when creating a new RequestListingViewHolder.
     * Assigns the list item layout to the new ViewHolder.
     *
     * @return A new RequestListingViewHolder using the list layout
     */


    @NonNull
    @Override
    public RequestListingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Create a new view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.book_listing_item_list, viewGroup, false);
        RequestListingViewHolder requestListingViewHolder = new RequestListingViewHolder(view);
        return requestListingViewHolder;
    }

    /**
     * Routine for binding new library to a list item
     *
     * @param requestListingViewHolder The ViewHolder to be assigned
     * @param position                 Index in the list to use for this list slot
     */
    @Override
    public void onBindViewHolder(@NonNull RequestListingViewHolder requestListingViewHolder, int position) {
        readLock.lock();
        //Get the library at the provided position
        final BookListing item = library.getBookAtPosition(position).clone();
        readLock.unlock();
        //Index to pass to the edit activity
        final int index = requestListingViewHolder.getAdapterPosition();

        //Fill the text fields with the object's library
        //requestListingViewHolder.bookThumbnail.//todo listing photo

        Bitmap thumbnailBitmap = item.getPhotoBitmap();
        if (thumbnailBitmap == null)
            requestListingViewHolder.bookThumbnail.setImageResource(R.drawable.ic_book_default);
        else
            requestListingViewHolder.bookThumbnail.setImageBitmap(thumbnailBitmap);

        requestListingViewHolder.bookTitleLabel.setText(item.getBook().getTitle());
        requestListingViewHolder.bookAuthorLabel.setText(item.getBook().getAuthor());
        requestListingViewHolder.isbnLabel.setText(item.getBook().getIsbn());
        requestListingViewHolder.ownerLabel.setVisibility(View.GONE);//Exclude this element
        requestListingViewHolder.ownedLabel.setVisibility(View.GONE);//Exclude this element
        requestListingViewHolder.statusLabel.setText(item.getStatus().toString());
        Log.d("mattTag", "really? " + item.getBook().toString() + " " + item.getStatus());

        /*if ((position & 1) == 1) {//check odd
            requestListingViewHolder.constraintLayout.setBackgroundColor(sourceActivity.getResources().getColor(R.color.lightDarkerTint));
        }*/

        //Add the click listener to the item
        requestListingViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedItem(item);
            }
        });

        if (requestListingViewHolder.allowAnimation) {
            AlphaAnimation animIn = new AlphaAnimation(0.0f, 1.0f);
            animIn.setDuration(500);
            requestListingViewHolder.itemView.startAnimation(animIn);
            ScaleAnimation anim2 = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim2.setDuration(500);
            anim2.setInterpolator(new OvershootInterpolator());
            requestListingViewHolder.itemView.startAnimation(anim2);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RequestListingViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d("jamie", "view detach window " + holder.toString());
        holder.itemView.clearAnimation();
        if (viewHolders.contains(holder.itemView)) {
            viewHolders.remove(holder.itemView);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RequestListingViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.allowAnimation = true;
        viewHolders.add(holder);
        //holder.itemView.clearAnimation();
    }

    /**
     * Click Listener for individual list items. Starts view activity for the clicked item.
     */
    public void clickedItem(BookListing item) {
        //Start View/Edit Activity with Clicked Item
        Intent intent = new Intent(sourceActivity, ListingViewActivity.class);
        if (item != null) {
            intent.putExtra("isbn", item.getBook().getIsbn());
            intent.putExtra("dupID", item.getDupInd());
            intent.putExtra("ownerUsername", item.getOwnerUsername());
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
    public static class RequestListingViewHolder extends RecyclerView.ViewHolder {

        //Layout Objects
        private ConstraintLayout constraintLayout;
        private ImageView bookThumbnail;
        private TextView bookTitleLabel;
        private TextView bookAuthorLabel;
        private TextView isbnLabel;
        private TextView ownerLabel;
        private TextView ownedLabel;
        private TextView statusLabel;

        private boolean allowAnimation = true;

        /**
         * Creates the RequestListingViewHolder
         *
         * @param itemView The view for this item
         */
        public RequestListingViewHolder(@NonNull View itemView) {
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
