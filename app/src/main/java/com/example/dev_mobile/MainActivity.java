package com.example.dev_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    public final static String WEIGHT = "com.example.Dev_Mobile.weight";
    public final static String HEIGHT = "com.example.Dev_Mobile.height";
    public final static String GENDER = "com.example.Dev_Mobile.gender";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        if(pref.contains("weight") && pref.contains("height") && pref.contains("gender")){
            Intent intent = new Intent(this, SecondActivity.class);
            String weight = pref.getString("weight", null);
            String height = pref.getString("height",null);
            String gender = pref.getString("gender",null);
            intent.putExtra(WEIGHT, weight);
            intent.putExtra(HEIGHT, height);
            intent.putExtra(GENDER, gender);
            startActivity(intent);
        }
        else{
            setContentView(R.layout.activity_main);
        }


    }

    public void submit(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        EditText eTweight = (EditText) findViewById(R.id.weight);
        EditText eTheight = (EditText) findViewById(R.id.height);
        ToggleButton tBgender = findViewById(R.id.gender);
        String gender = "m";
        if(tBgender.isChecked()){
            gender = "f";
        }
        String weight = eTweight.getText().toString();
        String height = eTheight.getText().toString();

        intent.putExtra(WEIGHT, weight);
        intent.putExtra(HEIGHT, height);
        intent.putExtra(GENDER, gender);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("weight", weight);
        editor.putString("height", height);
        editor.putString("gender", height);
        editor.commit();



        startActivity(intent);
    }
}
