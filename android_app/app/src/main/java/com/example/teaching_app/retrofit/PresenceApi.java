package com.example.teaching_app.retrofit;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import com.example.teaching_app.model.Presence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PresenceApi {
    @DELETE("/presence/remove")
    Call<Presence> removePresence(@Query("studentId") long studentId, @Query("lessonId") long lessonId);

    @PUT("/presence/removeAndAdd")
    Call<Void> removeAndAddPresence(@Query("studentId") long studentId, @Query("lessonId") long lessonId, @Query("date") LocalDateTime date,
                               @Query("presence") int presenceType);

    @GET("/presence/student/getPresences")
    Call<List<Object[]>> getPresences(@Query("studentId") long studentId, @Query("groupId") long groupId);
}
