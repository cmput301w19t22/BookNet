package com.example.booknet.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknet.GeocodingLocation;
import com.example.booknet.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Activity to select a location on a map.
 */
public class MapSelectActivity extends AppCompatActivity implements OnMapReadyCallback {

    public final float UAQUAD_LATITUDE = 53.5260290f;
    public final float UAQUAD_LONGITUDE = -113.5252808f;

    //Layout Objects
    ImageButton backButton;
    SupportMapFragment mapView;
    TextView latLongTV;
    Button addressButton;

    //Activity Data
    GoogleMap googleMap;
    Marker myMarker;

    private void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_select);

        //Get Layout Objects
        backButton = findViewById(R.id.backButton);
        addressButton = (Button) findViewById(R.id.searchButton);
        latLongTV = (TextView) findViewById(R.id.latLongTV);
        //mapView = findViewById(R.id.mapView);
        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);

        //Set Listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Setup Map
        mapView.getMapAsync(this);

        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                EditText editText = (EditText) findViewById(R.id.addressET);
                String address = editText.getText().toString();

                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(address,
                        getApplicationContext(), new GeocoderHandler());
            }
        });

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
    public void onMapReady(final GoogleMap googleMap) {
        myMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(UAQUAD_LATITUDE, UAQUAD_LONGITUDE)));
        latLongTV.setText("Latitude: " + UAQUAD_LATITUDE+"\nLongitude: " + UAQUAD_LONGITUDE);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(UAQUAD_LATITUDE, UAQUAD_LONGITUDE), 10f));

        googleMap.setBuildingsEnabled(true);
        //googleMap.setMapType(1);


        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                myMarker.setPosition(latLng);
                LatLng coordinates = myMarker.getPosition();

                Location location = new Location("LongPressLocationProvider");
                location.setLatitude(coordinates.latitude);
                location.setLongitude(coordinates.longitude);

                latLongTV.setText("Latitude: " + location.getLatitude()+"\nLongitude: " + location.getLongitude());
            }
        });


        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions();
            Log.d("jamie", "no map permission");
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        setGoogleMap(googleMap);
    }
}
