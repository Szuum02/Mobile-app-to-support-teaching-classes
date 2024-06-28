package com.example.teaching_app.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StudentApi {
    @GET("/student/showGroups")
    Call<List<Object[]>> findAllGroups(@Query("studentId") long studentId);
}
