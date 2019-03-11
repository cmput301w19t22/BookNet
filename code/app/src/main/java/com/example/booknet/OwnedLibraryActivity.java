package com.example.booknet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class OwnedLibraryActivity extends AppCompatActivity {

    //Layout Objects
    private RecyclerView libraryListView;
    private OwnedListingAdapter listingAdapter;
    private Button addButton;

    //App Data
    private BookLibrary data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_library);

        addButton = findViewById(R.id.addBookButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

        //todo get real data
        //data = CurrentUser.getInstance().getOwnedLibrary();
        data=MockDatabase.getInstance().readUserOwnedLibrary(CurrentUser.getInstance().getUserAccount().getUsername());

        fillLayout();//todo delete when using real db

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Update List Data
        listingAdapter.notifyDataSetChanged();
    }

    private void fillLayout(){

        //temp fake data
        //data = new BookLibrary();

        if (data == null) {
            Book b1 = new Book("Fake Book 1", "Author 1", "", "1234567890");
            Book b2 = new Book("Fake Book 2", "Author 2", "", "1234567891");
            UserAccount u1 = new UserAccount("myself", "debug");
            UserAccount currentUserAccount = CurrentUser.getInstance().getUserAccount().clone();
            //Log.d("useraccount",u1.toString());
            //Toast.makeText(this,"UserAccount: "+u1.toString(),Toast.LENGTH_LONG).show();
            data.addBookListing(new BookListing(b1, currentUserAccount));
            data.addBookListing(new BookListing(b2, currentUserAccount));
        }

        //Setup RecyclerView
        libraryListView = findViewById(R.id.bookLibrary);
        libraryListView.setLayoutManager(new LinearLayoutManager(this));
        listingAdapter = new OwnedListingAdapter(data, this);
        libraryListView.setAdapter(listingAdapter);
    }


    private void addBook() {
        Intent intent = new Intent(this, NewBookActivity.class);
        startActivity(intent);
    }
}
