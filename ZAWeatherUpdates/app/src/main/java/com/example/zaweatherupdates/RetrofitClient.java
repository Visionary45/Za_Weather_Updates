package com.example.zaweatherupdates;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private Api myAPI;

    private RetrofitClient(){
        Retrofit  retrofit = new Retrofit.Builder().baseUrl(myAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myAPI = retrofit.create(Api.class);
    }
    public static synchronized RetrofitClient getInstance(){
        if(instance == null){
            instance = new RetrofitClient();
        }
        return instance;
    }
    public Api getMyapi(){
        return myAPI;
    }
}
