package com.example.teachingapp.retrofit.Api;

import com.example.teachingapp.models.Lesson;
import com.example.teachingapp.models.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GroupApi {

    @GET("/group/showAll")
    Call<List<Object[]>> getStudents(@Query("groupId") long groupId);

    @GET("/group/lessons/showAll")
    Call<List<Object[]>> getLessons(@Query("groupId") long groupId);
}
