package com.example.dev_mobile;

import android.Manifest;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SecondActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensor;
    TextView steps;
    boolean running = false;

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

        String name = intent.getStringExtra(MainActivity.NAME);

        TextView TextView = (TextView) findViewById(R.id.name);
        steps = findViewById(R.id.steps);

        TextView.setText("Hello " + name);

        sensor = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    public void submit(View view) {
// Create an intent for the third activity
        Intent intent = new Intent(this, ThirdActivity.class);

// Start the activity
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running) {
            steps.setText("" + String.valueOf(event.values[0]));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor stepCounter = sensor.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        PackageManager pm = getPackageManager();
        if (!(pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER))) {
            Toast.makeText(this, "Your device doesn't include a step sensor", Toast.LENGTH_LONG).show();
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

    public void MapsOnClick(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


}
