package com.example.booknet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity for fulfilling a book exchange. Uses a scanner to verify the borrow took place.
 */
public class BorrowBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);
    }
}
