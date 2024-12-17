package com.example.teachingapp.retrofit.Api;

import com.example.teachingapp.dtos.AddGroupDTO;
import com.example.teachingapp.dtos.TeacherDTO;
import com.example.teachingapp.models.Group;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface TeacherApi {
    @GET("/teacher/showGroups")
    Call<List<Object[]>> findAllGroups(@Query("teacherId") long teacherId);

    @POST("/teacher/login")
    Call<TeacherDTO> teacherLogin(@Query("id") Long id);

    @POST("/teacher/add")
    Call<TeacherDTO> addTeacher(@Query("id") Long id, @Query("name") String name, @Query("lastName") String lastName);

    @PUT("/teacher/addLessons")
    Call<Void> addLessons(@Body List<AddGroupDTO> groups, @Query("teacherId") Long teacherId);
}
