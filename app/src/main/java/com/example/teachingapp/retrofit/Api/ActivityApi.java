package com.example.teachingapp.retrofit.Api;

import com.example.teachingapp.dtos.ActivityDTO;
import com.example.teachingapp.dtos.ActivityPlotDTO;
import com.example.teachingapp.dtos.ActivityRankingDTO;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ActivityApi {

    @POST("/activity/add")
    Call<Integer> addActivity(@Query("lessonId") long lessonId, @Query("studentId") long studentId,
                              @Query("date") LocalDateTime date, @Query("points") int points);

    @GET("/activity/ranking")
    Call<List<ActivityRankingDTO>> getRanking(@Query("groupId") long groupId);

    @GET("/activity/groupRanking")
    Call<List<ActivityRankingDTO>> getGroupRanking(@Query("groupId") long groupId);

    @GET("/activity/studentHistory")
    Call<List<ActivityDTO>> getStudentHistory(
            @Query("studentId") long studentId,
            @Query("groupId") long groupId);

    @GET("/activity/plot")
    Call<List<ActivityPlotDTO>> getPlot(
            @Query("studentId") long studentId,
            @Query("groupId") long groupId);
}
