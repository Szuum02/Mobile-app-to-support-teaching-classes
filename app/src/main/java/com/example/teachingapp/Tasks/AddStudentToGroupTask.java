package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.AddGroup;
import com.example.teachingapp.Student.StudentMainPage;
import com.example.teachingapp.Student._StudentChooseGroup;
import com.example.teachingapp.User._Login;
import com.example.teachingapp.dtos.LessonDTO;
import com.example.teachingapp.dtos.StudentDTO;
import com.example.teachingapp.retrofit.Api.StudentApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStudentToGroupTask {
    private final AddGroup activity;
    private final Long studentId;
    private Button addGroupButton;
    private Button returnButton;
    private EditText groupCodeText;
    private SharedPreferences sharedPreferences;

    public AddStudentToGroupTask(AddGroup activity, Long studentId, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.studentId = studentId;
        addGroupButton = activity.findViewById(R.id.add_group_button);
        returnButton = activity.findViewById(R.id.return_button);
        groupCodeText = activity.findViewById(R.id.group_code);
        this.sharedPreferences = sharedPreferences;
    }

    public void startTask() {
        setupAddButton();
        setupReturnButton();
    }

    private void setupAddButton() {
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupCode = groupCodeText.getText().toString();

                RetrofitService retrofitService = new RetrofitService();
                StudentApi studentApi = retrofitService.getRetrofit().create(StudentApi.class);
                studentApi.addGroup(studentId, groupCode).enqueue(new Callback<List<LessonDTO>>() {
                    @Override
                    public void onResponse(Call<List<LessonDTO>> call, Response<List<LessonDTO>> response) {
                        if (response.body().size() == 0) {
                            Toast.makeText(activity, "Niepoprawny kod grupy", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(activity, "Udało się dołączyć do grupy!", Toast.LENGTH_SHORT).show();
                        refreshStudentData();

                    }

                    @Override
                    public void onFailure(Call<List<LessonDTO>> call, Throwable t) {
                        Toast.makeText(activity, "Błąd serwera", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setupReturnButton() {
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, _StudentChooseGroup.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    private void refreshStudentData() {
        RetrofitService retrofitService = new RetrofitService();
        StudentApi studentApi = retrofitService.getRetrofit().create(StudentApi.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        studentApi.studentLogin(studentId).enqueue(new Callback<StudentDTO>() {
            @Override
            public void onResponse(Call<StudentDTO> call, Response<StudentDTO> response) {
                editor.putString("student_data", gson.toJson(response.body()));
                editor.putString("lessons", gson.toJson(response.body().getLessons()));
                editor.apply();
            }

            @Override
            public void onFailure(Call<StudentDTO> call, Throwable t) {
                Toast.makeText( activity, "Nie udało się zaktualizować listy lekcji",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
