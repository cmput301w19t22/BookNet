package com.example.booknet.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.Model.CurrentUser;
import com.example.booknet.DatabaseManager;
import com.example.booknet.Dialogs.InitialUserProfileDialog;
import com.example.booknet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;

public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener, InitialUserProfileDialog.InitialUserProfileListener {
    private final String TAG = "FB_SIGNIN";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etPass;
    private EditText etEmail;
    private DatabaseManager manager = DatabaseManager.getInstance();

    private String CHANNEL_ID = "BOOKNET_NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        manager.setOnLoginPage(true);

        setContentView(R.layout.activity_log_in);

        findViewById(R.id.btnCreate).setOnClickListener(this);
        findViewById(R.id.createAccountButton).setOnClickListener(this);

        etEmail = findViewById(R.id.etEmailAddr);
        etPass = findViewById(R.id.etPassword);

        // TODO: Get a reference to the Firebase auth object
        mAuth = FirebaseAuth.getInstance();


        // Async database initiation task
        // read all the database data to private attributes.
        // These data are updated in real time once the database changes.


        // TODO: Attach a new AuthListener to detect sign in and out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "Signed in: " + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "Currently signed out");
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO: add the AuthListener
        mAuth.addAuthStateListener(mAuthListener);

        String debugger = null;
        Log.d("mattTag", "fist");
        try  {

            Field debuggerField = Class.forName("com.example.booknet.Debugger").getDeclaredField("debuggerName");
            debuggerField.setAccessible(true);

            debugger = (String) debuggerField.get(null);

        }  catch (ClassNotFoundException e) {
            Log.d("mattTag", "nono");

        } catch (NoSuchFieldException e) {
            Log.d("mattTag", "noFF");
        } catch (IllegalAccessException e) {
            Log.d("mattTag", "ilililli");
        }
        Log.d("mattTag", "looo");

        String email = null;
        String password = null;

        if (debugger != null){
            String n = debugger.toLowerCase();
            if (n.equals("matt") || n.equals("jamie") || n.equals("jace") || n.equals("sean") || n.equals("seth") || n.equals("calvin")){
                email = n + "@debug.com";
                password = "123456";
            }
        }
        if (email != null){

            signIn(email, password);
        }



    }

    @Override
    public void onStop() {
        super.onStop();
        // TODO: Remove the AuthListener
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccountButton:
                signUserIn();
                break;

            case R.id.btnCreate:
                createUserAccount();
                break;

            /*case R.id.btnSignOut:
                signUserOut();
                break;*/
        }
    }

    private boolean checkFormFields() {
        String email, password;

        email = etEmail.getText().toString();
        password = etPass.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email Required");
            return false;
        }
        if (password.isEmpty()){
            etPass.setError("Password Required");
            return false;
        }

        return true;
    }

    private void updateStatus() {
        TextView tvStat = findViewById(R.id.tvSignInStatus);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvStat.setText("Signed in: " + user.getEmail());
        }
        else {
            tvStat.setText("Signed Out");
        }
    }



    private void updateStatus(String stat) {
        TextView tvStat = findViewById(R.id.tvSignInStatus);
        tvStat.setText(stat);
    }

    public void signUserIn() {
        if (!checkFormFields())
            return;

        final String email = etEmail.getText().toString();
        String password = etPass.getText().toString();
        signIn(email, password);



    }

    /*private void signUserOut() {
        // TODO: sign the user out
        mAuth.signOut();
        updateStatus();
    }*/

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

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginPageActivity.this, "Signed in", Toast.LENGTH_SHORT)
                                            .show();
                                    //Intent intent = new Intent(LoginPageActivity.this, UserProfile.class);
                                    //intent.putExtra("emailLabel", email);
                                    //startActivity(intent);

                                    //save current user for future use
                                    CurrentUser.getInstance().updateUser(mAuth.getCurrentUser());

                                    createNotificationChannel();

                                    //Shows in-progress dialog during connection
                                    // As user phone/name needs to be checked, connection needs to be established first
                                    //this method also takes care of following intents
                                    manager.connectToDatabase(LoginPageActivity.this);
                                }
                                else {
                                    Toast.makeText(LoginPageActivity.this, "Sign in failed", Toast.LENGTH_SHORT)
                                            .show();
                                }

                                updateStatus();
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            updateStatus("Invalid password.");
                        }
                        else if (e instanceof FirebaseAuthInvalidUserException) {
                            updateStatus("No account with this email.");
                        }
                        else {
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });


    }

    private void createUserAccount(){
        Intent intent = new Intent(this,AccountCreateActivity.class);
        startActivity(intent);
    }

    /* MOVED TO ACCOUNT CREATE ACTIVITY
    private void createUserAccount() {
        if (!checkFormFields())
            return;

        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        // TODO: Create the user account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginPageActivity.this, "User created", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    Toast.makeText(LoginPageActivity.this, "Account creation failed", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            updateStatus("This email address is already in use.");
                        }
                        else {
                            updateStatus(e.getLocalizedMessage());
                        }
                    }
                });
    }*/

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String email ,String phonenumber, String username) {

        //todo: validate phonenumber
        boolean takenUsername = manager.isUsernameTaken(username);
        Log.d("mattTag", phonenumber);
        Log.d("mattTag", username);
        if (takenUsername){
            Toast.makeText(LoginPageActivity.this, "Username taken!", Toast.LENGTH_SHORT)
                    .show();
        }
        else{

            Log.d("mattTag", "yeas");
            manager.writeUserProfile(email, phonenumber);
            manager.writeUsername(username);
            manager.writeUserPhone(phonenumber);
            CurrentUser.getInstance().setUsername(username);
            CurrentUser.getInstance().setAccountPhone(phonenumber);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }
    }

    public void onDestroy() {
        Log.d("mattTag", "destroyed haha");
        manager.setOnLoginPage(false);
        super.onDestroy();
    }


    public void promptInitialProfile(){
        DialogFragment dialog = new InitialUserProfileDialog();
        dialog.show(getSupportFragmentManager(), "InitialUserProfileDialog");

    }


    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
        CurrentUser.getInstance().logout();
        finish();
    }

    public void goToMainPage() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}

