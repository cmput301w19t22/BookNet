package com.example.booknet.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.CurrentUser;
import com.example.booknet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AccountCreateActivity extends AppCompatActivity {

    //Layout Objects
    private EditText usernameField;
    private EditText emailField;
    private EditText profileEmailField;
    private EditText phoneField;
    private EditText passwordField;
    private Button createButton;
    private ImageButton backButton;

    private boolean usernameWritten;
    private boolean phoneWritten;

    private int drawableCheckId = R.drawable.ic_check_black_24dp;
    private int drawableCrossId = R.drawable.ic_close_black_24dp;

    private String CHANNEL_ID = "BOOKNET_NOTIFICATION";

    ProgressDialog progressDialog;

    private DatabaseReference usernameRef;
    private ValueEventListener usernameListener;
    private ReentrantReadWriteLock l = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock usernameReadLock = l.readLock();
    private ReentrantReadWriteLock.WriteLock usernameWriteLock = l.writeLock();
    private ArrayList<String> usernames = new ArrayList<>();
    private boolean nameloaded;

    public String username;
    public String phone;
    public String profileEmail;


    // There are several async tasks to wait
    // 1. write username to database
    // 2. write userphone to database
    // 3. write user email to the phone
    // 4. create user account with email and password
    // 5. log in the user
    //  6. wait for database connection
    // notifyTaskFinished() will increase this by one each time a task is finished and trigger next activity upon final finish
    private int accountCreatingProgress;

    //Activity Data
    private FirebaseAuth mAuth;
    private DatabaseManager manager = DatabaseManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nameloaded = false;

        usernameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usernameWriteLock.lock();
                usernames.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String name = data.getKey();
                    usernames.add(name);
                }
                nameloaded = true;
                usernameWriteLock.unlock();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        usernameRef = FirebaseDatabase.getInstance().getReference("Usernames");
        usernameRef.addValueEventListener(usernameListener);


        accountCreatingProgress = 0;

        usernameWritten = false;
        phoneWritten = false;


        setContentView(R.layout.activity_account_create);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Trying to Create Account and signing in...");


        //Get Layout Objects
        usernameField = findViewById(R.id.usernameField);
        emailField = findViewById(R.id.etEmailAddr);
        profileEmailField = findViewById(R.id.profileEmailField);
        phoneField = findViewById(R.id.phoneField);
        passwordField = findViewById(R.id.etPassword);
        createButton = findViewById(R.id.createAccountButton);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameloaded)
                    createUserAccount();
                else
                    Toast.makeText(getApplicationContext(), "usernames not fetched from database", Toast.LENGTH_SHORT)
                            .show();

            }
        });

    }


    private void createUserAccount() {
        if (!checkFormFields())
            return;

        progressDialog.show();

        final String email = emailField.getText().toString();
        final String password = passwordField.getText().toString();
        profileEmail = profileEmailField.getText().toString();
        phone = phoneField.getText().toString();
        username = usernameField.getText().toString();


        if (isUsernameTaken(username)) {
            progressDialog.dismiss();
            usernameField.setError("Username taken!");
            updateStatus("Username " + username + " is taken");
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    notifyTaskFinished("User account created");
                                    Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT)
                                            .show();

                                    signIn(email, password);


                                } else {
                                    Toast.makeText(getApplicationContext(), "Account creation failed", Toast.LENGTH_SHORT)
                                            .show();
                                    progressDialog.dismiss();
                                }

                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("LOGIN", e.toString());
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            emailField.setError("Email already in use");
                            updateStatus("This email address is already in use.");
                        } else {
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });


    }

    private boolean isUsernameTaken(String username) {
        usernameReadLock.lock();
        boolean res = usernames.contains(username);
        usernameReadLock.unlock();
        return res;
    }


    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    notifyTaskFinished("User successfully signed in");

                                    CurrentUser.getInstance().updateUser(mAuth.getCurrentUser());

                                    createNotificationChannel();

                                    manager.connectToDatabase(AccountCreateActivity.this);
                                } else {
                                    Toast.makeText(AccountCreateActivity.this, "Sign in failed", Toast.LENGTH_SHORT)
                                            .show();
                                    progressDialog.dismiss();
                                }

                            }
                        });


    }


    private boolean checkFormFields() {
        String email, password, username, profileEmail, phone;

        email = emailField.getText().toString();
        password = passwordField.getText().toString();
        username = usernameField.getText().toString();
        boolean res = true;

        if (email.isEmpty()) {
            emailField.setError("Email Required");
            res = false;
        }
        if (password.isEmpty()) {
            passwordField.setError("Password Required");
            res = false;
        }
        if (username.isEmpty()) {
            usernameField.setError("Username Required");
            res = false;
        }
        if (username.isEmpty()) {
            usernameField.setError("Username Required");
            res = false;
        }

        return res;
    }


    private void updateStatus(String stat) {
        TextView tvStat = findViewById(R.id.tvSignInStatus);
        tvStat.setText(stat);
    }

    // synchronized ensures accountCreatingProgress to be incremented by one thread everytime to avoid missing counting
    public synchronized void notifyTaskFinished(String finishingMessage) {
        accountCreatingProgress += 1;

        // todo: have some progress bar effect?
        progressDialog.setMessage(finishingMessage);

        if (accountCreatingProgress == 6) {

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
            progressDialog.dismiss();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        usernameRef.removeEventListener(usernameListener);

    }

}
