package com.example.teaching_app.Tasks;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.teaching_app.DatabaseConnection;
import com.example.teaching_app.Teacher.ChooseGroup;
import com.example.teaching_app.model.Presence;
import com.example.teaching_app.retrofit.GroupApi;
import com.example.teaching_app.retrofit.PresenceApi;
import com.example.teaching_app.retrofit.RetrofitService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertPresence extends AsyncTask<Void, Void, Void>{

    private final long studentId;
    private final int presenceTypeId;
    private final long lessonId;
    private final Button button;
    private final List<Button> otherButtons;

    public InsertPresence(long studentId, int presenceTypeId, long lessonId, Button button, List<Button> otherButtons) {
        this.studentId = studentId;
        this.presenceTypeId = presenceTypeId;
        this.lessonId = lessonId;
        this.button = button;
        this.otherButtons = otherButtons;
    }


    // todo -> zmienić na bardziej aktualne metody (zachowując wywołanie asynchroniczne)
    @Override

    protected Void doInBackground(Void... voids) {
        RetrofitService retrofitService = new RetrofitService();
        PresenceApi presenceApi = retrofitService.getRetrofit().create(PresenceApi.class);

        LocalDateTime currentDate = LocalDateTime.now();

//        presenceApi.removePresence(studentId, lessonId)
//                .enqueue(new Callback<Presence>() {
//                    @Override
//                    public void onResponse(Call<Presence> call, Response<Presence> response) {
//                        Log.d("remove", "Success");
//                    }
//
//                    @Override
//                    public void onFailure(Call<Presence> call, Throwable t) {
//                        Log.e("SQLException", "Server error while removing");
//                    }
//                });

        presenceApi.removeAndAddPresence(studentId, lessonId, currentDate, presenceTypeId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("add", "Success");
                        changeColour();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("SQLException", "Server error while adding presence");
                    }
                });
        return null;
    }

    private void changeColour() {
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
        for (Button otherButton : otherButtons) {
            otherButton.setBackgroundColor(0xFF808080);
        }
    }
}
