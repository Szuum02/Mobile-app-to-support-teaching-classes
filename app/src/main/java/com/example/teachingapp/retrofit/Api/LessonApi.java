package com.example.teachingapp.retrofit.Api;

import com.example.teachingapp.models.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LessonApi {

    @GET("/lesson/showStudents")
    Call<List<Object[]>> getStudents(@Query("lessonId") long lessonId);
}
