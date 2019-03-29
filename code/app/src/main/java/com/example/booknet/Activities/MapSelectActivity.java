package com.example.booknet.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.booknet.DatabaseManager;
import com.example.booknet.Model.BookListing;
import com.example.booknet.Model.UserLocation;
import com.example.booknet.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activity to select and/or view a location on a map.
 */
public class MapSelectActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final float mapZoomDefault = 16.5f;
    public static final String MEETUP_TITLE = "Meetup Spot: %s for %s";
    public final float UAQUAD_LATITUDE = 53.5260290f;
    public final float UAQUAD_LONGITUDE = -113.5252808f;

    //Layout Objects
    ImageButton backButton;
    ImageButton gotoMarkerButton;
    Button selectButton;
    SupportMapFragment mapView;
    TextView latLongTV;
    EditText noteField;

    //Activity Data
    GoogleMap googleMap;
    Marker myMarker;
    BookListing listing;
    boolean editMode = true;

    private DatabaseManager manager = DatabaseManager.getInstance();

    /**
     * Simple method to set the map in this activity.
     *
     * @param googleMap The map.
     */
    private void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }


    /**
     * Called when creating the activity
     * Sets up listeners and inits the map.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_select);

        //Get Intent
        Intent intent = getIntent();
        //Check if given info to fetch listing
        if (intent.hasExtra("username") && intent.hasExtra("bookisbn")) {
            String username = intent.getStringExtra("username");
            String isbn = intent.getStringExtra("bookisbn");
            int dupID = intent.getIntExtra("dupID", 0);
            listing = manager.readBookListingOfUsername(username, isbn, dupID);
        }
        if (intent.hasExtra("username")) {
            editMode = intent.getBooleanExtra("editmode", false);
        } else {
            editMode = false;
        }


        //Get Layout Objects
        backButton = findViewById(R.id.backButton);
        gotoMarkerButton = findViewById(R.id.gotoMarkerButton);
        selectButton = findViewById(R.id.selectButton);
        noteField = findViewById(R.id.noteText);
        //mapView = findViewById(R.id.mapView);
        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);

        if (!editMode) {
            noteField.setVisibility(View.GONE);

        }

        //Set Listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gotoMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMarker != null) {
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), mapZoomDefault));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(myMarker.getPosition()));
                }
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMode && myMarker != null) {
                    String note = noteField.getText().toString();
                    listing.setGeoLocation(new UserLocation(myMarker.getPosition(), note));
                    manager.writeUserBookListing(listing);
                }
            }
        });

        //Setup Map
        mapView.getMapAsync(this);

        latLongTV = (TextView) findViewById(R.id.latLongLabel);


    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setGoogleMap(googleMap);

        myMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(UAQUAD_LATITUDE, UAQUAD_LONGITUDE)));
        //myMarker.setTitle(MEETUP_TITLE);
        if (listing != null) {
            fetchListingLocation(listing, myMarker);
        }
        latLongTV.setText("Latitude: " + UAQUAD_LATITUDE + "\nLongitude: " + UAQUAD_LONGITUDE);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), mapZoomDefault));

        googleMap.setBuildingsEnabled(true);
        //googleMap.setMapType(1);


        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (editMode) {
                    myMarker.setPosition(latLng);
                    //myMarker.setTitle(MEETUP_TITLE);
                    LatLng coordinates = myMarker.getPosition();

                    Location location = new Location("LongPressLocationProvider");
                    location.setLatitude(coordinates.latitude);
                    location.setLongitude(coordinates.longitude);


                    latLongTV.setText("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());
                }
            }
        });


        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            Log.d("jamie", "no map permission");
            return;
        }

        //Actions if we have access to user's location
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng userLatLong = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));


    }


    private void fetchListingLocation(BookListing listing, Marker marker) {
        UserLocation location = listing.getGeoLocation();
        if (location != null) {
            marker.setPosition(location.getLocation());
            marker.setTitle(String.format(MEETUP_TITLE, listing.getOwnerUsername(), listing.getBook().getTitle()));
        }
    }
}
