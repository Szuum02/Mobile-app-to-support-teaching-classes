package com.example.teachingapp.retrofit;

import com.google.gson.Gson;

import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    @Getter
    private Retrofit retrofit;

    public RetrofitService() {
        //change to your IP address
        String BASE_URL = "http://192.168.0.64:8080";
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

}
