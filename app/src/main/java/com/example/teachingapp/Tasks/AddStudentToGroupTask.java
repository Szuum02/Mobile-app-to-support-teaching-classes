package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.AddGroup;
import com.example.teachingapp.Student.StudentMainPage;
import com.example.teachingapp.Student._StudentChooseGroup;
import com.example.teachingapp.dtos.LessonDTO;
import com.example.teachingapp.retrofit.Api.StudentApi;
import com.example.teachingapp.retrofit.RetrofitService;

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

    public AddStudentToGroupTask(AddGroup activity, Long studentId) {
        this.activity = activity;
        this.studentId = studentId;
        addGroupButton = activity.findViewById(R.id.add_group_button);
        returnButton = activity.findViewById(R.id.return_button);
        groupCodeText = activity.findViewById(R.id.group_code);
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
                        // todo -> dodać nowe lekcje do studenta (to śmieszne co widać w całym programie)

                        goToMainPage();
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
            }
        });
    }

    private void goToMainPage() {
        Intent intent = new Intent(activity, StudentMainPage.class);
        activity.startActivity(intent);
    }
}
