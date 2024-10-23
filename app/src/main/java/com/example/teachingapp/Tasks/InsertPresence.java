package com.example.teachingapp.Tasks;

import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.teachingapp.retrofit.Api.PresenceApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertPresence {

    private final long studentId;
    private final int presenceTypeId;
    private final long lessonId;
    private final Button button;
    private final List<Button> unpressedButtons;

    public InsertPresence(long studentId, int presenceTypeId, long lessonId,
                          Button button, List<Button> unpressedButtons) {
        this.studentId = studentId;
        this.presenceTypeId = presenceTypeId;
        this.lessonId = lessonId;
        this.button = button;
        this.unpressedButtons = unpressedButtons;
    }

    protected void removeAndAddPresence() {
        RetrofitService retrofitService = new RetrofitService();
        PresenceApi presenceApi = retrofitService.getRetrofit().create(PresenceApi.class);

        presenceApi.removeAndAddPresence(studentId, lessonId, LocalDateTime.now(), presenceTypeId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        Log.d("add", "Success");
                        changeButtonColour();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e("SQLException", "Server error while adding presence");
                    }
                });
    }

    private void changeButtonColour() {
        switch (presenceTypeId) {
            case 1:
                button.setBackgroundColor(0xFF00FF00);
                break;
            case 2:
                button.setBackgroundColor(0xFFFF0000);
                break;
            case 3:
                button.setBackgroundColor(0xFFFFFF00);
                break;
        }
        for (Button otherButton : unpressedButtons) {
            otherButton.setBackgroundColor(0xFF808080);
        }
    }
}
