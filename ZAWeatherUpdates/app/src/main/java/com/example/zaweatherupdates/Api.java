package com.example.zaweatherupdates;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    public String BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast";
    @GET("forecast")
    Call<List<WeatherResults>> getWeatherDetails();

}
