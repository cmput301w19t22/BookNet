package com.example.booknet.Adapters;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
 * Intended for viewing listings owned by other users.
 *
 * @author Jamie
 * @version 1.0
 */
public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.BookListingViewHolder> {

    //The list of BookListings to display
    private BookLibrary data;
    private ReentrantReadWriteLock.ReadLock readLock;

    //The activity this adapter was created from
    private FragmentActivity sourceActivity;

    private ArrayList<View> views = new ArrayList<>();
    private ArrayList<BookListingViewHolder> viewHolders = new ArrayList<>();

    private Drawable bookDefault;
    private boolean allowNewAnimation = true;

    public void setAllowNewAnimation(boolean allowNewAnimation) {
        Log.d("jamie", "animation " + (allowNewAnimation ? "on" : "off"));
        this.allowNewAnimation = allowNewAnimation;
    }

    /**
     * Creates the adapter
     *
     * @param data           The list of BookListings to use for the list display
     * @param sourceActivity The activity that created this adapter
     * @param readLock
     */
    public BookSearchAdapter(BookLibrary data, final FragmentActivity sourceActivity, ReentrantReadWriteLock.ReadLock readLock) {
        this.data = data;
        this.sourceActivity = sourceActivity;
        this.readLock = readLock;
        bookDefault = sourceActivity.getDrawable(R.drawable.ic_book_default);
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                Log.d("jamie", "data observer on change");
                for (BookListingViewHolder holder : viewHolders) {
                    if (holder != null) {
                        Log.d("jamie", "disable animation for " + holder.toString());
                        //if (holder.bookThumbnail.getDrawable() == bookDefault) {
                        //    holder.allowAnimation = true;
                        //} else {
                        holder.allowAnimation = false;
                        //}
                    } else {
                        viewHolders.remove(holder);
                    }
                }
                //setAllowNewAnimation(false);
            }
        });
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
        viewHolders.add(bookListingViewHolder);
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
        readLock.lock();
        Log.d("jamie", "view bind " + bookListingViewHolder.toString());
        //Get the data at the provided position
        final BookListing item = data.getBookAtPosition(position);
        //Index to pass to the edit activity
        final int index = bookListingViewHolder.getAdapterPosition();

        //Fill the text fields with the object's data
        if (item.getPhotoBitmap() != null) {
            Log.d("mattX", "yeah boi");
            bookListingViewHolder.bookThumbnail.setImageBitmap(item.getPhotoBitmap());
        } else {
            Log.d("mattX", "nahnah");
            bookListingViewHolder.bookThumbnail.setImageResource(R.drawable.ic_book_default);
        }

        bookListingViewHolder.bookTitleLabel.setText(item.getBook().getTitle());
        bookListingViewHolder.bookAuthorLabel.setText(item.getBook().getAuthor());
        bookListingViewHolder.isbnLabel.setText(item.getBook().getIsbn());
        bookListingViewHolder.ownerLabel.setText(item.getOwnerUsername());
        bookListingViewHolder.statusLabel.setText(item.getStatus().toString());
        bookListingViewHolder.item = item;

        /*if ((position & 1) == 1) {//check odd
            bookListingViewHolder.constraintLayout.setBackgroundColor(sourceActivity.getResources().getColor(R.color.lightDarkerTint));
        }*/

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

        readLock.unlock();

        if (bookListingViewHolder.allowAnimation) {
            AlphaAnimation animIn = new AlphaAnimation(0.0f, 1.0f);
            animIn.setDuration(500);
            bookListingViewHolder.itemView.startAnimation(animIn);
            final ScaleAnimation anim2 = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim2.setDuration(500);
            anim2.setInterpolator(new OvershootInterpolator());
            bookListingViewHolder.itemView.startAnimation(anim2);
            //anim2.cancel();
            Log.d("jamie", "animated list item " + bookListingViewHolder.toString());
        } else {
            Log.d("jamie", "not allowed animation for " + bookListingViewHolder.toString());
        }
    }

    @Override
    public void onViewRecycled(@NonNull BookListingViewHolder holder) {
        super.onViewRecycled(holder);
        Log.d("jamie", "view recycle " + holder.toString());
        //holder.setAllowAnimation(false);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BookListingViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d("jamie", "view detach window " + holder.toString());
        holder.itemView.clearAnimation();
        if (views.contains(holder.itemView)) {
            views.remove(holder.itemView);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BookListingViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.d("jamie", "view attach window " + holder.toString());
        holder.allowAnimation = true;
        views.add(holder.itemView);
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

        private boolean allowAnimation = true;

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
