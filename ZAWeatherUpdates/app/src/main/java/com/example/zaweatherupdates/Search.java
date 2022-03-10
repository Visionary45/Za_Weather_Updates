package com.example.zaweatherupdates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Search extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextInputLayout txtCityname;
    Button btnSrch;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        txtCityname = findViewById(R.id.cityEdit);

        btnSrch = findViewById(R.id.btncitySearch);

        btnSrch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             cityName = txtCityname.getEditText().getText().toString();
             Intent search = new Intent();
             search.putExtra("CityName", cityName);
             setResult(Activity.RESULT_OK, search);
             finish();
            }
        });

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Search");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
