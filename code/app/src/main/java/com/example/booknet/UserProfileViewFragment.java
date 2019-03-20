package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView usernameLabel;
    private TextView phoneLabel;
    private TextView emailLabel;
    private TextView ratingLabel;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private Button reviewsButton;
    private Button booksButton;
    private Button editButton;
    private Button logoutButton;
    private int starOff = R.drawable.ic_star_border_24dp;
    private int starOn = R.drawable.ic_star_24dp;

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
                if (all != null && p != null){
                    userProfile.putAll(all.get(CurrentUser.getInstance().getUID()));
                    notifyProfileChange();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        manager.getAllProfileRef().addValueEventListener(listener);

        //Obtain References To Layout Objects
        usernameLabel = view.findViewById(R.id.userNameLabel);
        phoneLabel = view.findViewById(R.id.phoneNumberLabel);
        emailLabel = view.findViewById(R.id.emailLabel);
        ratingLabel = view.findViewById(R.id.ratingTextLabel);
        star1 = view.findViewById(R.id.ratingStar1);
        star2 = view.findViewById(R.id.ratingStar2);
        star3 = view.findViewById(R.id.ratingStar3);
        star4 = view.findViewById(R.id.ratingStar4);
        star5 = view.findViewById(R.id.ratingStar5);
        reviewsButton = view.findViewById(R.id.reviewsButton);
        booksButton = view.findViewById(R.id.libraryButton);
        editButton = view.findViewById(R.id.editButton);
        logoutButton = view.findViewById(R.id.logoutButton);



        fillLayout();
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        booksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewUserBooks(userAccount);
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

        // todo: fix rating score
//        ratingLabel.setText(String.format("%1.1f", userAccount.getRatingScore()));
        ratingLabel.setText("0.0" );

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
        manager.getAllProfileRef().removeEventListener(listener);
        super.onDestroy();

    }

}
