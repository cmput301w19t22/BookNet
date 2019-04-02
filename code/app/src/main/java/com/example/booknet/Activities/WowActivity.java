package com.example.booknet.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.booknet.GifImageView;
import com.example.booknet.R;

public class WowActivity extends AppCompatActivity {

    static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaPlayer = MediaPlayer.create(this, R.raw.wow);

        setContentView(R.layout.activity_wow);

        final GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.newwow);


//        Intent mainIntent = new Intent(WowActivity.this, MainActivity.class);
//        WowActivity.this.startActivity(mainIntent);
//
//        //Finish splash activity so user cant go back to it.
//        WowActivity.this.finish();
//
//        //Apply splash exit (fade out) and main entry (fade in) animation transitions.
//        overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Create an intent that will start the main activity.
                Intent mainIntent = new Intent(WowActivity.this, MainActivity.class);
                WowActivity.this.startActivity(mainIntent);

                //Finish splash activity so user cant go back to it.
                WowActivity.this.finish();

                //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);

            }
        }, 400);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mediaPlayer.start();

            }
        }, 700);

    }

}

