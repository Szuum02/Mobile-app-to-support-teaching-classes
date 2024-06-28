package com.example.teaching_app.retrofit;

import com.example.teaching_app.model.Activity;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ActivityApi {
    // todo ->  czy nie da się zrobić PUT
    @POST("/activity/add")
    Call<Integer> addActivity(@Query("lessonId") long lessonId, @Query("studentId") long studentId,
                              @Query("date") LocalDateTime date, @Query("points") int points);
}
