package com.example.teaching_app.retrofit;

import com.example.teaching_app.model.Group;

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
