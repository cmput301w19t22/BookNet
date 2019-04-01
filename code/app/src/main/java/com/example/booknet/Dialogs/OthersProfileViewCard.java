package com.example.booknet.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booknet.Activities.ReviewListViewActivity;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.CurrentUser;
import com.example.booknet.Model.Review;
import com.example.booknet.R;

// https://developer.android.com/guide/topics/ui/dialogs#java
public class OthersProfileViewCard extends DialogFragment {

    //Layout Objects
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private TextView ratingAverageText;
    private TextView ratingCountText;

    private int starOff = R.drawable.ic_star_border_24dp;
    private int starOn = R.drawable.ic_star_24dp;
    private int starHalf = R.drawable.ic_star_half_24dp;

    //Dialog Data
    private boolean isOutdated;
    private String username;
    private String phone;
    private String email;
    private float userRatingAverage;
    private int userRatingCount;

    private DatabaseManager manager = DatabaseManager.getInstance();

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    /**
     * create a profile card for the owner of a booklisting
     *
     * @param l the book listing
     * @return the profile card DialogFragment
     */
    public static OthersProfileViewCard newInstance(BookListing l) {
        OthersProfileViewCard f = new OthersProfileViewCard();

        Bundle args = new Bundle();
        String phone = l.getOwnerPhone();
        if (phone == null) {
            args.putBoolean("outdated", true);
        } else {
            args.putBoolean("outdated", false);
            args.putString("username", l.getOwnerUsername());
            args.putString("phone", phone);
            args.putString("email", l.getOwnerEmail());
        }


        f.setArguments(args);

        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isOutdated = getArguments().getBoolean("outdated");
        username = getArguments().getString("username");
        phone = getArguments().getString("phone");
        email = getArguments().getString("email");
        userRatingAverage = manager.readUserReviewAverage(username);
        userRatingCount = manager.readUserReviewCount(username);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        if (isOutdated) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Cannot Read User Profile");
            builder.setMessage("User profile read " +
                    "failed because the target user " +
                    "used an outdated version of app. " +
                    "This has been fixed in the latest " +
                    "update. You will be able to " +
                    "read his/her profile upon his/her " +
                    "first log-in in the new app version");
            return builder.create();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            final LayoutInflater inflater = requireActivity().getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_profile_card, null);
            TextView usernameText = dialogView.findViewById(R.id.others_username);
            usernameText.setText(username);

            TextView phoneText = dialogView.findViewById(R.id.others_phonenumber);
            if (phone.equals("")) {
                phoneText.setText("---");
            } else {
                phoneText.setText(phone);
            }

            TextView emailText = dialogView.findViewById(R.id.other_profile_email);
            if (email.equals("")) {
                emailText.setText("---");
            } else {
                emailText.setText(email);
            }

            star1 = dialogView.findViewById(R.id.ratingStar1);
            star2 = dialogView.findViewById(R.id.ratingStar2);
            star3 = dialogView.findViewById(R.id.ratingStar3);
            star4 = dialogView.findViewById(R.id.ratingStar4);
            star5 = dialogView.findViewById(R.id.ratingStar5);
            ratingAverageText = dialogView.findViewById(R.id.others_rating);
            ratingCountText = dialogView.findViewById(R.id.numReviews);

            int[] stars = new int[]{starOff, starHalf, starOn};
            star1.setImageResource(Review.starImage(userRatingAverage, 0, stars));
            star2.setImageResource(Review.starImage(userRatingAverage, 1, stars));
            star3.setImageResource(Review.starImage(userRatingAverage, 2, stars));
            star4.setImageResource(Review.starImage(userRatingAverage, 3, stars));
            star5.setImageResource(Review.starImage(userRatingAverage, 4, stars));
            if (userRatingAverage >= 0) {
                ratingAverageText.setText(String.format("%1.1f", userRatingAverage));
            } else {
                ratingAverageText.setText("---");
            }
            ratingCountText.setText(String.format("(%d Ratings)", userRatingCount));

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            ImageButton leaveButton = dialogView.findViewById(R.id.leave_button);
            leaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            Button reviewsButton = dialogView.findViewById(R.id.reviewsButton);
            reviewsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ReviewListViewActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            });


            ImageButton addReviewButton = dialogView.findViewById(R.id.addReviewButton);
            addReviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReviewCreateDialog reviewCreateDialog = ReviewCreateDialog.newInstance(CurrentUser.getInstance().getUsername(), username);
                    reviewCreateDialog.show(getActivity().getSupportFragmentManager(), "Create Review");
                }
            });

            builder.setView(dialogView);


            return builder.create();

        }
    }
}
