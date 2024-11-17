package com.example.teachingapp.retrofit.Api;

import com.example.teachingapp.dtos.TeacherDTO;
import com.example.teachingapp.dtos.UserDTO;
import com.example.teachingapp.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {

    @GET("/user/showAll")
    Call<List<User>> getAllUsers();

    @POST("/user/login")
    Call<UserDTO> getUserByMail(@Query("mail") String mail, @Query("password") String password);

    @POST("/user/add")
    Call<Long> addUser(@Query("mail") String mail, @Query("password") String password, @Query("isStudent") boolean isStudent);

    @GET("/user/checkUniqueMail")
    Call<Boolean> checkUniqueMail(@Query("mail") String mail);

    @POST("/user/addTeacher")
    Call<TeacherDTO> addTeacher(@Query("name") String name, @Query("lastName") String lastName,
                                @Query("mail") String mail, @Query("password") String password);
}
