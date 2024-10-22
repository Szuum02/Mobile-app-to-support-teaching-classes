package com.example.teachingapp.retrofit.Api;

import com.example.teachingapp.models.Presence;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface PresenceApi {

    @DELETE("/presence/remove")
    Call<Presence> removePresence(@Query("studentId") long studentId, @Query("lessonId") long lessonId);

    @PUT("/presence/removeAndAdd")
    Call<Void> removeAndAddPresence(@Query("studentId") long studentId, @Query("lessonId") long lessonId, @Query("date") LocalDateTime date,
                                    @Query("presence") int presenceType);

    @GET("/presence/student/getPresences")
    Call<List<Object[]>> getPresences(@Query("studentId") long studentId, @Query("groupId") long groupId);
}
