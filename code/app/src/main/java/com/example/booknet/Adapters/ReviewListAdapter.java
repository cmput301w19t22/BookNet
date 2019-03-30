package com.example.booknet.Adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booknet.Model.Review;
import com.example.booknet.R;

import java.util.ArrayList;

/**
 * Adapter for displaying a review in a recycler view list.
 *
 * @author Jamie
 */
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewListViewHolder> {

    //The list of reviews to display
    private ArrayList<Review> reviews;

    //The activity this adapter was created from
    private AppCompatActivity sourceActivity;

    //Image Drawables to use in this activity
    private int starOn = R.drawable.ic_star_24dp;
    private int starOff = R.drawable.ic_star_border_24dp;

    /**
     * Creates the adapter.
     *
     * @param reviews        List of reviews to display
     * @param sourceActivity The activity that created this adapter.
     */
    public ReviewListAdapter(ArrayList<Review> reviews, AppCompatActivity sourceActivity) {
        this.reviews = reviews;
        this.sourceActivity = sourceActivity;
    }

    /**
     * Routine when creating a new ReviewListViewHolder.
     * Assigns the list item layout to the new ViewHolder.
     *
     * @return A new ReviewListViewHolder using the list layout
     */
    @NonNull
    @Override
    public ReviewListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Create a new view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_item_list, viewGroup, false);
        ReviewListViewHolder newMeasurementViewHolder = new ReviewListViewHolder(view);
        return newMeasurementViewHolder;
    }

    /**
     * Routine for binding new data to a list item
     *
     * @param reviewListViewHolder The ViewHolder to be assigned
     * @param position         Index in the list to use for this list slot
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewListViewHolder reviewListViewHolder, int position) {
        //Get the data at the provided position
        final Review review = reviews.get(position);

        //Fill the text fields with the object's data
        reviewListViewHolder.reviewerName.setText(review.getReviewerUsername());
        reviewListViewHolder.reviewComment.setText(review.getMessage());
        float score = review.getScore();
        reviewListViewHolder.ratingLabel.setText(String.format("%1.1f", score));
        reviewListViewHolder.star1.setImageResource((score >= 1) ? starOn : starOff);
        reviewListViewHolder.star2.setImageResource((score >= 2) ? starOn : starOff);
        reviewListViewHolder.star3.setImageResource((score >= 3) ? starOn : starOff);
        reviewListViewHolder.star4.setImageResource((score >= 4) ? starOn : starOff);
        reviewListViewHolder.star5.setImageResource((score >= 5) ? starOn : starOff);

        if ((position & 1) == 1) {//check odd
            reviewListViewHolder.constraintLayout.setBackgroundColor(sourceActivity.getResources().getColor(R.color.lightDarkerTint));
        }

        //Set Click Listeners
        //todo any click listeners?
    }

    /**
     * Override to get the number of items in the dataset
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return reviews.size();
    }

    /**
     * Stores the view data for a list item.
     */
    public class ReviewListViewHolder extends RecyclerView.ViewHolder {

        //Layout Objects
        private ConstraintLayout constraintLayout;
        private TextView reviewerName;
        private TextView ratingLabel;
        private TextView reviewComment;
        private ImageView star1;
        private ImageView star2;
        private ImageView star3;
        private ImageView star4;
        private ImageView star5;

        /**
         * Creates the ReviewListViewHolder
         *
         * @param itemView The view for this item
         */
        public ReviewListViewHolder(@NonNull View itemView) {
            super(itemView);

            //Obtain Layout Object References
            constraintLayout = itemView.findViewById(R.id.reviewLayout);
            reviewerName = itemView.findViewById(R.id.reviewerNameLabel);
            ratingLabel = itemView.findViewById(R.id.ratingTextLabel);
            reviewComment = itemView.findViewById(R.id.reviewComment);
            star1 = itemView.findViewById(R.id.ratingStar1);
            star2 = itemView.findViewById(R.id.ratingStar2);
            star3 = itemView.findViewById(R.id.ratingStar3);
            star4 = itemView.findViewById(R.id.ratingStar4);
            star5 = itemView.findViewById(R.id.ratingStar5);
        }
    }
}
