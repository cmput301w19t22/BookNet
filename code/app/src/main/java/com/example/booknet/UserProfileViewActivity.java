package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    //Data
    UserAccount userAccount;
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view);

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

        //Get Profile
        Intent intent = getIntent();
        //Check that we have a username to check
        if (!intent.hasExtra("username")) {
            Toast.makeText(this, "View Not Passed With Username", Toast.LENGTH_LONG).show();
            //finish();
        } else {
            //Obtain the UserAccount for the username
            username = intent.getStringExtra("username");
            if (intent.hasExtra("isMe")) {
                //Get local copy if viewing own account
                //userAccount = CurrentUser.getInstance().getUserAccount();
                userAccount = fetchUser(username);
                editButton.setVisibility(View.VISIBLE);
            } else {
                userAccount = fetchUser(username);
            }
            fillLayout();
        }

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
    }

    /**
     * Fills the layout with info from the user account
     */
    private void fillLayout() {
        if (userAccount == null) {
            Toast.makeText(this, "User Not Found: " + username, Toast.LENGTH_LONG).show();
            //finish();
        } else {
            //Fill Layout With Info
            usernameLabel.setText(userAccount.getUsername());
            phoneLabel.setText(userAccount.getProfile().getPhoneNumber());
            emailLabel.setText(userAccount.getProfile().getEmail());
            ratingLabel.setText(String.format("%1.1f", userAccount.getRatingScore()));
        }
    }

    /**
     * Obtains the UserAccount from the database
     *
     * @param username
     * @return
     */
    private UserAccount fetchUser(String username) {
        return MockDatabase.getInstance().readUserAccount(username);
    }

    /**
     * Starts the activity to edit the user profile
     */
    private void editProfile() {
        Intent intent = new Intent(this, ProfileEditActivity.class);
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
}
