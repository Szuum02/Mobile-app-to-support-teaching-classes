package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.dtos.TeacherDTO;
import com.example.teachingapp.retrofit.Api.UserApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class _TeacherRegistration extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String name;
    private String lastName;
    private RetrofitService retrofitService = new RetrofitService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._registration_form_login);
        Intent intent = getIntent();
        if (intent != null) {
            sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            name = intent.getStringExtra("name");
            lastName = intent.getStringExtra("lastName");
        }
    }

    public void registerHandler(View view) {
        EditText mailText = findViewById(R.id.login);
        EditText passwordText = findViewById(R.id.password);
        EditText confirmPasswordText = findViewById(R.id.confirmPassword);

        checkDataAndRegister(mailText, passwordText, confirmPasswordText);
    }

    private void checkDataAndRegister(EditText mailText, EditText passwordText, EditText confirmPasswordText) {
        String mail = mailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if (!password.equals(confirmPassword)) {
            confirmPasswordText.setError("Hasła są różne");
        }

        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        userApi.checkUniqueMail(mail).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.body()) {
                    mailText.setError("Konto już istnieje");
                    return;
                }
                registerTeacher(mail, password);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // TODO -> handle error
            }
        });
    }

    private void registerTeacher(String mail, String password) {
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        userApi.addTeacher(name, lastName, mail, password).enqueue(new Callback<TeacherDTO>() {
            @Override
            public void onResponse(Call<TeacherDTO> call, Response<TeacherDTO> response) {
                goToTeacherMainPage(response.body());
            }

            @Override
            public void onFailure(Call<TeacherDTO> call, Throwable t) {
                // TODO -> handle error
            }
        });
    }

    private void goToTeacherMainPage(TeacherDTO teacherDTO) {
        Intent intent = new Intent(this, TeacherMainPage.class);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        intent.putExtra("teacher_id", teacherDTO.getId());
        editor.putString("lessons", gson.toJson(teacherDTO.getLessons()));
        editor.putString("teacher_data", gson.toJson(teacherDTO));
        editor.apply();
        startActivity(intent);
    }

}
