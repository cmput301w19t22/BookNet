package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

/**
 * Activity to view a user's profile.
 *
 * @author Jamie
 * @version 1.0
 */
public class UserProfileViewActivity extends AppCompatActivity {

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
    private DatabaseManager manager = DatabaseManager.getInstance();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);

//        Intent i = getIntent();
//        if (i.hasExtra("Email")){
//            userProfile.put("Email", i.getStringExtra("Email"));
//            userProfile.put("Email", i.getStringExtra("Email"));
//        }

        userProfile = manager.readUserProfile();

        //Obtain References To Layout Objects
        usernameLabel = findViewById(R.id.userNameLabel);
        phoneLabel = findViewById(R.id.phoneNumberLabel);
        emailLabel = findViewById(R.id.emailLabel);
        ratingLabel = findViewById(R.id.ratingTextLabel);
        star1 = findViewById(R.id.ratingStar1);
        star2 = findViewById(R.id.ratingStar2);
        star3 = findViewById(R.id.ratingStar3);
        star4 = findViewById(R.id.ratingStar4);
        star5 = findViewById(R.id.ratingStar5);
        reviewsButton = findViewById(R.id.reviewsButton);
        booksButton = findViewById(R.id.libraryButton);
        editButton = findViewById(R.id.editButton);
        logoutButton = findViewById(R.id.logoutButton);



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

                startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
                finish();
            }
        });
        //#endregion
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
        Intent intent = new Intent(this, ProfileEditActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
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
}
