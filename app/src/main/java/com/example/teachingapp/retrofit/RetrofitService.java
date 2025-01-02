package com.example.teachingapp.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    @Getter
    private Retrofit retrofit;

    //change to your IP address
    private static String baseUrl = "http://192.168.202.6:8080";

    public RetrofitService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static void setBaseUrl(String newBaseUrl) {
        baseUrl = newBaseUrl;
    }
}
