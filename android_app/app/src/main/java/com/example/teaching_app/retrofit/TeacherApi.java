package com.example.teaching_app.retrofit;

import com.example.teaching_app.model.Group;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TeacherApi {
    @GET("/teacher/showGroups")
    Call<List<Object[]>> findAllGroups(@Query("teacherId") long teacherId);
}
