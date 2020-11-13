package com.example.dev_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public final static String NAME = "com.example.Dev_Mobile.name";
    public final static String PASSWORD = "com.example.Dev_Mobile.email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        if(pref.contains("name") && pref.contains("password")){
            Intent intent = new Intent(this, SecondActivity.class);
            String name = pref.getString("name", null);
            String password = pref.getString("password",null);
            intent.putExtra(NAME, name);
            intent.putExtra(PASSWORD, password);
            startActivity(intent);
        }
        else{
            setContentView(R.layout.activity_main);
        }


    }

    public void submit(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        EditText eTname = (EditText) findViewById(R.id.name);
        EditText eTpassword = (EditText) findViewById(R.id.password);
        String name = eTname.getText().toString();
        String password = eTpassword.getText().toString();
        intent.putExtra(NAME, name);
        intent.putExtra(PASSWORD, password);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", name);
        editor.putString("password", password);
        editor.commit();



        startActivity(intent);
    }
}
