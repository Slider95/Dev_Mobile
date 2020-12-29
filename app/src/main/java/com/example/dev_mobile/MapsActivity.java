package com.example.dev_mobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static String TAG = "ThirdActivity";
    public static final int PERMISSIONS_REQUEST = 0;

    public static final String LOCATION = "location";

    private Location mLocation;
    private String locationProvider;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView speed;
    private TextView move;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        speed = findViewById(R.id.speed);
        move = findViewById(R.id.move);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set location provider.
        locationProvider = LocationManager.GPS_PROVIDER;
        // Or use LocationManager.NETWORK_PROVIDER

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);



        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the location provider
                Log.d(TAG, "Location changed");
               // Toast.makeText(getApplicationContext(), "Location changed, updating map...",
                 //       Toast.LENGTH_SHORT).show();
                mLocation = location;
                if (mMap != null) {
                    updateMap();
                }
                if (location.hasSpeed()) {
                    speed.setText(location.getSpeed() + " km/h");
                    if(location.getSpeed() <= 0){
                        move.setText("Still");
                    }
                    else if(location.getSpeed() <= 8){
                        move.setText("Walking");
                    }
                    else if(location.getSpeed() <= 25){
                        move.setText("Running");
                    }
                    else{
                        move.setText("In a vehicule");
                    }
                } else {
                    speed.setText("0 km/h");
                }
            }


            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        requestLocationUpdates();

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove the listener you previously added
        if(locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }

    }

    @Override
    public void onRequestPermissionsResult(int request, String permissions[], int[] results) {
        switch (request) {
            case PERMISSIONS_REQUEST: {

                // If request is cancelled, the result arrays are empty
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted, yay! Do something useful
                    requestLocationUpdates();

                } else {

                    // Permission was denied, boo! Disable the
                    // functionality that depends on this permission
                    Toast.makeText(this, "Permission denied to access device's location", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker on our location.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "The map is ready!");
        mMap = googleMap;
        updateMap();
    }

    private void updateMap() {
        Log.d(TAG, "Updating map...");
        if (mMap != null) {
            mMap.clear();
            if (mLocation == null) {
                mLocation = new Location(locationProvider);
                mLocation.setLatitude(0);
                mLocation.setLongitude(0);
            }
            LatLng pin = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pin,12.0f));
            mMap.addMarker(new MarkerOptions().position(pin).title("Current location"));
        }
    }

    private void requestLocationUpdates() {
        // Check permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Permission already granted
            locationManager.requestLocationUpdates(
                    locationProvider, 1000, 1, locationListener);

        } else {

            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);

        }
    }

}