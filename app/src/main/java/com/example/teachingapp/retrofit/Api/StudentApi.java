package com.example.teachingapp.retrofit.Api;

import com.example.teachingapp.dtos.StudentDTO;
import com.example.teachingapp.models.Group;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StudentApi {
    @POST("/student/login")
    Call<StudentDTO> studentLogin(@Query("studentId") long studentId);
}
