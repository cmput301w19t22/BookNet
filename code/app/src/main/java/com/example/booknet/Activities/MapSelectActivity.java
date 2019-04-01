package com.example.booknet.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.DatabaseManager;
import com.example.booknet.GeocodingLocation;
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
    SearchView searchBar;
    EditText noteField;
    TextView noteLabel;

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
     * Sets up listeners and initializes the map.
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
        if (intent.hasExtra("editmode")) {
            editMode = intent.getBooleanExtra("editmode", false);
        } else {
            editMode = false;
        }
        if (listing == null) {
            //no point staying if the listing doesn't exist
            Log.d("jamie", "Map activity given null listing");
            finish();
        }

        //Get Layout Objects
        backButton = findViewById(R.id.backButton);
        gotoMarkerButton = findViewById(R.id.gotoMarkerButton);
        selectButton = findViewById(R.id.selectButton);
        noteField = findViewById(R.id.noteText);
        noteLabel = findViewById(R.id.noteLabel);
        searchBar = findViewById(R.id.searchField);
        //addressButton = (Button) findViewById(R.id.searchButton);
        //searchText = (EditText) findViewById(R.id.addressField);
        latLongTV = (TextView) findViewById(R.id.latLongLabel);
        //mapView = findViewById(R.id.mapView);
        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);

        selectButton.setEnabled(false);
        if (editMode) {
            noteLabel.setVisibility(View.GONE);

        } else {
            noteField.setVisibility(View.GONE);
            noteLabel.setVisibility(View.VISIBLE);
            selectButton.setVisibility(View.GONE);
            searchBar.setVisibility(View.GONE);
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
                    if (myMarker.isVisible()) {
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), mapZoomDefault));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(myMarker.getPosition()));
                    }
                }
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editMode && myMarker != null) {
                    if (myMarker.isVisible()) {
                        String note = noteField.getText().toString();
                        listing.setGeoLocation(new UserLocation(myMarker.getPosition(), note));
                        manager.overwriteUserBookListing(listing);
                        selectButton.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Location Set", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });

        /*addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                String address = searchText.getText().toString();

                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(address,
                        getApplicationContext(), new GeocoderHandler());
            }
        });*/

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(s,
                        getApplicationContext(), new GeocoderHandler());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        //Setup Map
        mapView.getMapAsync(this);


    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            double Latitude;
            double Longitude;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    Latitude = bundle.getDouble("Latitude");
                    Longitude = bundle.getDouble("Longitude");
                    googleMap.clear();
                    myMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude)));
                    break;
                default:
                    locationAddress = null;
            }
            latLongTV.setText(locationAddress);
        }
    }

    /**
     * Request the permissions required for using the map with the user's location.
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    /**
     * Once we have received permissions, retry getting the map.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mapView.getMapAsync(this);
    }

    /**
     * Once the map is ready, this method sets up it's controls and the marker for the
     * booklisting this map was called for.
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        setGoogleMap(googleMap);

        myMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(UAQUAD_LATITUDE, UAQUAD_LONGITUDE)));
        //myMarker.setTitle(MEETUP_TITLE);
        latLongTV.setText("Latitude: " + UAQUAD_LATITUDE + "\nLongitude: " + UAQUAD_LONGITUDE);

        boolean fetched = false;
        if (listing != null) {
            fetched = fetchListingLocation(listing, myMarker);
            myMarker.setTitle(String.format(MEETUP_TITLE, listing.getOwnerUsername(), listing.getBook().getTitle()));
            latLongTV.setText("Latitude: " + myMarker.getPosition().latitude
                    + "\nLongitude: " + myMarker.getPosition().longitude);
            noteField.setText(listing.getGeoLocation().getNote());
            noteLabel.setText(listing.getGeoLocation().getNote());
        }

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
                    myMarker.setVisible(true);
                    LatLng coordinates = myMarker.getPosition();

                    Location location = new Location("LongPressLocationProvider");
                    location.setLatitude(coordinates.latitude);
                    location.setLongitude(coordinates.longitude);

                    selectButton.setEnabled(true);
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
        if (userLocation != null && !fetched) {
            LatLng userLatLong = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLong));
        }


    }


    /**
     * Obtains the location assigned to the given listing and applies it to the given map marker.
     * If the listing does not have a location, it will not be set.
     * Returns whether a valid location was found.
     *
     * @param listing The listing to check
     * @param marker  The marker to modify
     */
    private boolean fetchListingLocation(BookListing listing, Marker marker) {
        UserLocation location = listing.getGeoLocation();
        if (location != null) {
            if (location.locationSet()) {
                marker.setPosition(location.getLocation());
                marker.setTitle(String.format(MEETUP_TITLE, listing.getOwnerUsername(), listing.getBook().getTitle()));
                marker.setVisible(true);
                latLongTV.setText("Latitude: " + location.getLocation().latitude + "\nLongitude: " + location.getLocation().longitude);
                return true;
            } else {
                //Listing didn't have a valid location, so disable this marker
                marker.setVisible(false);
                return false;
            }
        }
        return false;
    }
}
