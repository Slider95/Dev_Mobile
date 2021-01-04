package com.example.dev_mobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SecondActivity extends AppCompatActivity implements SensorEventListener, CompoundButton.OnCheckedChangeListener {
    public static final int PERMISSIONS_REQUEST = 0;
    SensorManager sensor;
    TextView steps;
    TextView kms;
    TextView kcal;
    CheckBox toggleSteps;
    Sensor stepCounter;
    String weight;
    String height;
    String gender;
    float currSpeed;
    Double stepLength;
    boolean running = false;
    public static String TAG = "SecondActivity";
    private String locationProvider;
    private LocationManager locationManager;
    private LocationListener locationListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        Intent intent = getIntent();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            //ask for permission

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
            }
        }
        toggleSteps = findViewById(R.id.toggleSteps);
        toggleSteps.setOnCheckedChangeListener(this::onCheckedChanged);
        weight = intent.getStringExtra(MainActivity.WEIGHT);
        height = intent.getStringExtra(MainActivity.HEIGHT);
        gender = intent.getStringExtra(MainActivity.GENDER);
        stepLength = 0.415 * Integer.valueOf(height); //( 0.415 * height for males, 0.413 for females)
        if (gender.equals("f")) {
            stepLength = 0.413 * Integer.valueOf(height);
        }
        TextView TextView = (TextView) findViewById(R.id.weight);
        steps = findViewById(R.id.steps);
        kms = findViewById(R.id.kms);
        kcal = findViewById(R.id.kcal);
        sensor = (SensorManager) getSystemService(SENSOR_SERVICE);
//////////////////////////////////////////////////////////GPS///////////////////////////////////////////////


        // Check permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);

        }

        // Set location provider.
        locationProvider = LocationManager.GPS_PROVIDER;
        // Or use LocationManager.NETWORK_PROVIDER

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (location.hasSpeed()) {
                    currSpeed = location.getSpeed();
                }
            }
        };
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            steps.setText("" + String.valueOf(event.values[0]));
            Double v = (stepLength * event.values[0])/100000;
            kms.setText(String.format("%.2f", v) + " kms");
            Double v2 = v * Double.valueOf(weight);
            kcal.setText(String.format("%.2f", v2) + "Kcal");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        stepCounter = sensor.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        PackageManager pm = getPackageManager();
        if (!(pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER))) {
            Toast.makeText(this, "Your device doesn't include a step sensor", Toast.LENGTH_SHORT).show();
        }
        else {
            if (stepCounter != null) {
                sensor.registerListener(this, stepCounter, sensor.SENSOR_DELAY_UI);
            } else {
                Toast.makeText(this, "Step sensor not found !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onRequestPermissionsResult(int request, String permissions[], int[] results) {
        switch (request) {
            case PERMISSIONS_REQUEST: {

                // If request is cancelled, the result arrays are empty
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay! Do something useful
                } else {

                    // Permission was denied, boo! Disable the
                    // functionality that depends on this permission
                    Toast.makeText(this, "Permission denied to access device's GPS", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 1:
                // If request is cancelled, the result arrays are empty
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay! Do something useful
                } else {

                    // Permission was denied, boo! Disable the
                    // functionality that depends on this permission
                    Toast.makeText(this, "Permission denied to access step sensor", Toast.LENGTH_SHORT).show();
                }
                return;
        }

    }

    public void MapsOnClick(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    @Override
    public void onCheckedChanged(CompoundButton toggleSteps,
                                 boolean isChecked) {
        Log.d(TAG, "Checkbox changed");
        if (isChecked && stepCounter != null) {
            sensor.registerListener(this, stepCounter,
                    SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            sensor.unregisterListener(this);
        }
    }

}
