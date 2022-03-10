package com.example.zaweatherupdates;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showApplication();

    }
    private void showApplication(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent startApp = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startApp);
                finish();
            }
        }, 1000);
    }
}
