package com.example.teachingapp.Tasks;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.SettingsActivityStudent;
import com.example.teachingapp.dtos.ShowInRankingDTO;
import com.example.teachingapp.retrofit.Api.StudentApi;
import com.example.teachingapp.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsTaskStudent {
    private SettingsActivityStudent activity;
    private Button noButton;
    private Button yesButton;
    private Button returnButton;
    private Long studentId;

    public SettingsTaskStudent(SettingsActivityStudent activity, Long studentId) {
        this.activity = activity;
        this.noButton = activity.findViewById(R.id.no_button);
        this.yesButton = activity.findViewById(R.id.yes_button);
        this.returnButton = activity.findViewById(R.id.return_button);
        this.studentId = studentId;
    }

    public void startTask() {
        setUpNoButton();
        setUpYesButton();
        setUpReturnButton();
    }

    private void setUpNoButton() {
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RetrofitService retrofitService = new RetrofitService();
                StudentApi studentApi = retrofitService.getRetrofit().create(StudentApi.class);
                studentApi.setShowInRanking(studentId, false).enqueue(new Callback<ShowInRankingDTO>() {
                    @Override
                    public void onResponse(Call<ShowInRankingDTO> call, Response<ShowInRankingDTO> response) {
                        Toast.makeText(activity, "Twoje dane nie będą widoczne w rankingu", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<ShowInRankingDTO> call, Throwable t) {
                        Toast.makeText(activity, "Nie udało się przesłać danych, spróbuj ponownie", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    private void setUpYesButton() {
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RetrofitService retrofitService = new RetrofitService();
                StudentApi studentApi = retrofitService.getRetrofit().create(StudentApi.class);
                studentApi.setShowInRanking(studentId, true).enqueue(new Callback<ShowInRankingDTO>() {
                    @Override
                    public void onResponse(Call<ShowInRankingDTO> call, Response<ShowInRankingDTO> response) {
                        Toast.makeText(activity, "Twoje dane będą widoczne w rankingu", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<ShowInRankingDTO> call, Throwable t) {
                        Toast.makeText(activity, "Nie udało się przesłać danych, spróbuj ponownie", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    private void setUpReturnButton() {
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }
}

