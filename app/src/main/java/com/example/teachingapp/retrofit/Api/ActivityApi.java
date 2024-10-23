package com.example.teachingapp.retrofit.Api;

import com.example.teachingapp.Tasks.InsertActivity;
import com.example.teachingapp.models.Activity;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ActivityApi {

    @POST("/activity/add")
    Call<Integer> addActivity(@Query("lessonId") long lessonId, @Query("studentId") long studentId,
                              @Query("date") LocalDateTime date, @Query("points") int points);

    @GET("/activity/ranking")
    Call<List<Object[]>> getRanking(@Query("groupId") long groupId);

}
