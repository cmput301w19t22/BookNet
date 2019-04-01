package com.example.booknet.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.booknet.Activities.LoginPageActivity;
import com.example.booknet.Activities.ProfileEditActivity;
import com.example.booknet.Activities.ReviewListViewActivity;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.CurrentUser;
import com.example.booknet.Model.Review;
import com.example.booknet.Model.UserAccount;
import com.example.booknet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Activity to view a user's profile.
 *
 * @author Jamie
 * @version 1.0
 */
public class UserProfileViewFragment extends Fragment {

    //Layout Objects
    private ConstraintLayout profileView;
    private ConstraintLayout reviewsView;
    private TextView usernameLabel;
    private TextView phoneLabel;
    private TextView emailLabel;
    private TextView ratingLabel;
    private TextView ratingCountLabel;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private Button reviewsButton;
    private ImageButton editButton;
    private ImageButton logoutButton;
    private int starOff = R.drawable.ic_star_border_24dp;
    private int starOn = R.drawable.ic_star_24dp;
    private int starHalf = R.drawable.ic_star_half_24dp;

    private DatabaseManager manager = DatabaseManager.getInstance();
    private ValueEventListener listener;

    private static final String tag = "own_profile_view_fragment";

    public static UserProfileViewFragment newInstance() {
        UserProfileViewFragment myFragment = new UserProfileViewFragment();

        Bundle args = new Bundle();
//        args.putInt("someInt", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }

    //Activity Data
    UserAccount userAccount;
    String username = "";
    float userRatingAverage;
    int userRatingCount;
    HashMap<String, String> userProfile = new HashMap<String, String>();

    /**
     * Called when creating the activity
     * Gets the intent and sets click listeners
     *
     * @param savedInstanceState
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_profile_view, container, false);
        Log.d("seanTag", "onCreateView Profile");

//        Intent i = getIntent();
//        if (i.hasExtra("Email")){
//            userProfile.put("Email", i.getStringExtra("Email"));
//            userProfile.put("Email", i.getStringExtra("Email"));
//        }

        userProfile = manager.readCurrentUserProfile();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile.clear();
                HashMap<String, HashMap<String, String>> all = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                HashMap<String, String> p = all.get(CurrentUser.getInstance().getUID());
                if (all != null && p != null) {
                    userProfile.putAll(all.get(CurrentUser.getInstance().getUID()));
                    notifyProfileChange();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        manager.getAllProfileRef().addValueEventListener(listener);
        userRatingAverage = manager.readUserReviewAverage(CurrentUser.getInstance().getUsername());
        userRatingCount = manager.readUserReviewCount(CurrentUser.getInstance().getUsername());

        //Obtain References To Layout Objects
        profileView = view.findViewById(R.id.profile);
        reviewsView = view.findViewById(R.id.ratings);
        usernameLabel = view.findViewById(R.id.userNameLabel);
        phoneLabel = view.findViewById(R.id.phoneNumberLabel);
        emailLabel = view.findViewById(R.id.emailLabel);
        ratingLabel = view.findViewById(R.id.ratingTextLabel);
        ratingCountLabel = view.findViewById(R.id.numReviews);
        star1 = view.findViewById(R.id.ratingStar1);
        star2 = view.findViewById(R.id.ratingStar2);
        star3 = view.findViewById(R.id.ratingStar3);
        star4 = view.findViewById(R.id.ratingStar4);
        star5 = view.findViewById(R.id.ratingStar5);
        reviewsButton = view.findViewById(R.id.reviewsButton);

        editButton = view.findViewById(R.id.editButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        fillLayout();
        //#region Set Listeners
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });


        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewListViewActivity.class);
                intent.putExtra("username", CurrentUser.getInstance().getUsername());
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurrentUser.getInstance().logout();

                startActivity(new Intent(getContext(), LoginPageActivity.class));
                getActivity().finish();
            }
        });
        //#endregion

        return view;
    }

    /**
     * Fills the layout with info from the user account
     */
    private void fillLayout() {
        usernameLabel.setText(CurrentUser.getInstance().getUsername());
        Log.d("mattTag", "lmao");
//        Log.d("mattTag", userProfile.get("Phone"));
        phoneLabel.setText(userProfile.get("Phone"));
        emailLabel.setText(userProfile.get("Email"));

        // Fill Rating Section
        int[] stars = new int[]{starOff, starHalf, starOn};
        star1.setImageResource(Review.starImage(userRatingAverage, 0, stars));
        star2.setImageResource(Review.starImage(userRatingAverage, 1, stars));
        star3.setImageResource(Review.starImage(userRatingAverage, 2, stars));
        star4.setImageResource(Review.starImage(userRatingAverage, 3, stars));
        star5.setImageResource(Review.starImage(userRatingAverage, 4, stars));
        if (userRatingAverage >= 0) {
            ratingLabel.setText(String.format("%1.1f", userRatingAverage));
        } else {
            ratingLabel.setText("---");
        }
        ratingCountLabel.setText(String.format("(%d Ratings)", userRatingCount));


    }

    @Override
    public void onResume() {
        super.onResume();

        ScaleAnimation anim2 = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        anim2.setDuration(500);
        anim2.setInterpolator(new OvershootInterpolator());
        if (profileView != null) {
            profileView.startAnimation(anim2);
        }
        if (reviewsView != null) {
            reviewsView.startAnimation(anim2);
        }
    }

    /**
     * Obtains the UserAccount from the database
     *
     * @param username
     * @return
     */
    private UserAccount fetchUser(String username) {
        DatabaseManager.getInstance().readUserAccount(username);
        return CurrentUser.getInstance().getUserAccount();//todo get ANY user from db
    }

    /**
     * Starts the activity to edit the user profile
     */
    private void editProfile() {
        Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);

    }

    /**
     * Starts the activity to view the user's owned library.
     *
     * @param user
     */
    private void viewUserBooks(UserAccount user) {
        //todo: implement
    }

    public TextView getPhoneLabel() {
        return phoneLabel;
    }

    public TextView getEmailLabel() {
        return emailLabel;
    }

    public void notifyProfileChange() {

        phoneLabel.setText(userProfile.get("Phone"));
        emailLabel.setText(userProfile.get("Email"));
    }


    public void onDestroy() {
        try {
            manager.getAllProfileRef().removeEventListener(listener);
        } catch (NullPointerException e) {
            //todo who knows
        }

        super.onDestroy();

    }

}
