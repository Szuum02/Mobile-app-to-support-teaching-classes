package com.example.teaching_app.retrofit;

import com.example.teaching_app.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    @GET("/user/showAll")
    Call<List<User>> getAllUsers();

    @POST("/user/mail")
    Call<User> getUserByMail(@Query("mail") String mail, @Query("password") String password);
}
