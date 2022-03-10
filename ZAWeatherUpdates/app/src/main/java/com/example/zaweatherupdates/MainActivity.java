package com.example.zaweatherupdates;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton locationButton;
    DrawerLayout drawerLayout;
    TextView name, mxTemp,mnTemp, Temp, Condition, Humidity,Windspeed, realFeal, time;
    ImageView imgWeather, multi;
    private FusedLocationProviderClient client;
    ProgressBar bar;
    private  double latitude;
    private double longitude;
    CardView weatherData;
    private Activity Main;
    private RecyclerView weatherDetails;
    private  ArrayList<WeatherResults> weatherResults;
    WeatherAdapter weatherAdapter;

    public final String API_KEY = "ECDHQD2MLVGUG5NXZYUWBDZ4J";
    public String CityNameSrch = "";
    //Launching activity for result
    ActivityResultLauncher<Intent> searchIntent = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("SearchValue", "onActivityResult: ");
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent searchData = result.getData();
                        if(searchData != null){
                            CityNameSrch = searchData.getStringExtra("CityName");
                            Toast.makeText(getApplicationContext(), CityNameSrch, Toast.LENGTH_SHORT).show();
                                bar.setVisibility(View.GONE);
                                weatherData.setVisibility(View.VISIBLE);
                                getWeatherDetails(CityNameSrch);
                            }
                    }
                }
            }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize activity
        Main = MainActivity.this;

        name = findViewById(R.id.txtName);
        mxTemp = findViewById(R.id.txtmxTemp);
        mnTemp = findViewById(R.id.txtmnTemp);
        imgWeather = findViewById(R.id.imgCndtion);
        Condition = findViewById(R.id.txtCndtion);
        bar = findViewById(R.id.ldBar);
        weatherData = findViewById(R.id.weathrID);
        weatherDetails = findViewById(R.id.moreFcst);
        weatherResults = new ArrayList<>();
        bottomAppBar = findViewById(R.id.bottomAppBar);

        //setting up the recycleview
        recyclerviewMethod();


        //getting fused location long and lat
        client = LocationServices.getFusedLocationProviderClient(this);

        locationButton = findViewById(R.id.loction);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeatherDLtLn(latitude, longitude);
            }
        });
        checkLocalPermission();
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    latitude = Math.round(location.getLatitude() * 100.00)/ 100.0;

                    longitude = Math.round(location.getLongitude() * 100.00)/ 100.0;
                    getWeatherDLtLn(latitude, longitude);
                }else{
                    getWeatherDetails(CityNameSrch);
                }
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                 switch(item.getItemId())
                 {
                     case R.id.search:
                         Intent search = new Intent(MainActivity.this, Search.class);
                         searchIntent.launch(search);
                         break;
                 }
                return false;
            }
        });
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    public void getWeatherDetails(String Cname){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+Cname+"?unitGroup=metric&key="+API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.d("Error", "Request Failed");

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!response.isSuccessful()){
                    throw new IOException("Error : "+ response);
                }else{
                    Log.d("Error", "Request Successful.");
                }
                String res =  response.body().string();
                Main.runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        try{
                            JSONObject object = new JSONObject(res);
                            String city = object.getString("address");
                            JSONArray arr =object.getJSONArray("days");

                            JSONObject mxT = arr.getJSONObject(0);
                            double maxTemp = mxT.getDouble("temp");
                            double minTemp = mxT.getDouble("tempmin");
                            String icon = mxT.getString("icon");
                            String dateToday = mxT.getString("datetime");
                            String Con = mxT.getString("description");
                            SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
                            Date d = today.parse(dateToday);
                            mxTemp.setText(""+maxTemp+" °C");
                            mnTemp.setText(""+dateToday+"");
                            name.setText(city);
                            Condition.setText(Con);
                            if(response.isSuccessful()){
                                bar.setVisibility(View.GONE);
                                weatherData.setVisibility(View.VISIBLE);
                            }else{
                                bar.setVisibility(View.VISIBLE);
                                weatherData.setVisibility(View.GONE);
                            }
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Log.d("Check ", e.getMessage());
                        }
                        fillRecyclerviewName();
                    }

                });
            }
        });
    }
    public void getWeatherDLtLn(double lat, double lon ) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + lat + "," + lon +"?unitGroup=metric&key=" + API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.d("Error", "Request Failed");

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Error : " + response);
                } else {
                    Log.d("Error", "Request Successful.");
                }
                String res = response.body().string();
                Main.runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(res);
                            JSONArray arr = object.getJSONArray("days");

                            JSONObject mxT = arr.getJSONObject(0);
                            double maxTemp = mxT.getDouble("temp");
                            double minTemp = mxT.getDouble("tempmin");
                            String icon = mxT.getString("icon");
                            String dateToday = mxT.getString("datetime");
                            String Con = mxT.getString("description");
                            SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
                            Date d = today.parse(dateToday);
                            getIcon(icon);
                            mxTemp.setText("" + maxTemp + " °C");
                            mnTemp.setText("" + dateToday + "");
                            Condition.setText(Con);
                            Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                            try{
                                List<Address> address = geocoder.getFromLocation(latitude,longitude,1);
                                String add = address.get(0).getSubLocality();
                                String city = address.get(0).getSubLocality();
                                name.setText(city);
                                if(response.isSuccessful()){
                                    bar.setVisibility(View.GONE);
                                    weatherData.setVisibility(View.VISIBLE);
                                }else{
                                    bar.setVisibility(View.VISIBLE);
                                    weatherData.setVisibility(View.GONE);
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                                Log.d("Check ", e.getMessage());
                            }
                            fillRecyclerview();
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Log.d("Check ", e.getMessage());
                        }
                    }
                });
            }
        });
    }


    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
    }

    //permission Check
    private void checkLocalPermission() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{ACCESS_FINE_LOCATION}, 1);

            }
        }
    }
    public void getIcon(String Icon){

        if (Icon.equals("rain")){
            imgWeather.setImageResource(R.drawable.rain);
        }else if(Icon.equals("sunny")){
            imgWeather.setImageResource(R.drawable.sunny);
        }else if (Icon.equals("cloudy")){
            imgWeather.setImageResource(R.drawable.cloudy);
        }else if (Icon.equals("wind")){
            imgWeather.setImageResource(R.drawable.windy);
        }else if (Icon.equals("partly-cloudy-day")){
            imgWeather.setImageResource(R.drawable.partlycloudy);
        }else if (Icon.equals("snow")){
            imgWeather.setImageResource(R.drawable.snow);
        }
    }

    public void recyclerviewMethod(){
        weatherAdapter = new WeatherAdapter(MainActivity.this, weatherResults);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        weatherDetails.setHasFixedSize(false);

        weatherDetails.setLayoutManager(manager);
        weatherDetails.setNestedScrollingEnabled(false);

        weatherDetails.setAdapter(weatherAdapter);
    }

    //fill the recycler with current user location information
    public void fillRecyclerview(){
        if(latitude != 0 && longitude != 0){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
                            + latitude+"%2C"+longitude+"/next5days?unitGroup=metric&elements=datetime%2Cname%2Caddress" +
                            "%2CresolvedAddress%2Ctemp%2Cconditions%2Cdescription%2Cicon%2Csource&key=ECDHQD2MLVGUG5NXZYUWBDZ4J&contentType=json")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    Log.d("Error", "Request Failed");
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Error : " + response);
                    } else {
                        Log.d("Error", "Request Successful.");
                    }
                    String res = response.body().string();
                    Main.runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(res);
                                JSONArray arr = object.getJSONArray("days");
                                for(int i = 1; i < arr.length(); i++){
                                    JSONObject weather = arr.getJSONObject(i);

                                    double temp = weather.getDouble("temp");
                                    String day = weather.getString("datetime");
                                    String des = weather.getString("description");
                                    String Icon = weather.getString("icon");
                                    weatherResults.add(new WeatherResults(day, temp,Icon,des));
                                    recyclerviewMethod();
                                }
                            } catch (JSONException  e) {
                                Log.d("Check ", e.getMessage());
                            }
                        }
                    });
                }
            });
        }else {

        }
    }

    //method to fill the recyclerview with name search
    public void fillRecyclerviewName(){
        if (!CityNameSrch.equals("")){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/\n" +
                            "timeline/"+CityNameSrch+"%2C/next5days?iconSet=icons&unitGroup=metric&key=ECDHQD2MLVGUG5NXZYUWBDZ4J&contentType=json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    Log.d("Error", "Request Failed");

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(!response.isSuccessful()){
                        throw new IOException("Error : "+ response);
                    }else{
                        Log.d("Error", "Request Successful.");
                    }
                    String res =  response.body().string();
                    Main.runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            try{
                                JSONObject object = new JSONObject(res);
                                String city = object.getString("address");
                                JSONArray arr =object.getJSONArray("days");

                                for(int i = 1; i < arr.length(); i++){
                                    JSONObject weather = arr.getJSONObject(i);

                                    double temp = weather.getDouble("temp");
                                    String day = weather.getString("datetime");
                                    String des = weather.getString("description");
                                    String Icon = weather.getString("icon");
                                    weatherResults.add(new WeatherResults(day, temp,Icon,des));
                                    recyclerviewMethod();
                                }
                                if(response.isSuccessful()){
                                    bar.setVisibility(View.GONE);
                                    weatherData.setVisibility(View.VISIBLE);
                                }else{
                                    bar.setVisibility(View.VISIBLE);
                                    weatherData.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("Check ", e.getMessage());
                            }
                        }
                    });
                }
            });
        }
    }

}