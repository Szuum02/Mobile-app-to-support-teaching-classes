package com.example.teachingapp.Tasks;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.teachingapp.retrofit.Api.ActivityApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertActivity {

    private final long studentId;
    private final long lessonId;
    private final int points;
    private final String fullName;
    private final TextView nameView;
    private TextView pointsView;

    public InsertActivity(long studentId, long lessonId, int points, String fullName, TextView nameView, TextView pointsView) {
        this.studentId = studentId;
        this.lessonId = lessonId;
        this.points = points;
        this.fullName = fullName;
        this.nameView = nameView;
        this.pointsView = pointsView;
    }

    public void addActivity(){
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);
        activityApi.addActivity(lessonId, studentId, LocalDateTime.now(), points).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                updateText(response.body());
                Log.e("Working", response.body().toString());
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                Log.e("SQLException", "Server error while adding activity");
            }
        });
    }

    private void updateText(int points) {
        String pointsText = (points < 0) ? String.valueOf(points) : String.format("+%d", points);
        nameView.setText(String.format("%s ", fullName));
        pointsView.setText(pointsText);
    }
}
