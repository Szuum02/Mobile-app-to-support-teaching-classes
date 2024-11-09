package com.example.teachingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.Student.ChooseSubject;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.User.RegistrationForm;
import com.example.teachingapp.dtos.StudentDTO;
import com.example.teachingapp.dtos.StudentHistoryDTO;
import com.example.teachingapp.dtos.TeacherDTO;
import com.example.teachingapp.dtos.UserDTO;
import com.example.teachingapp.retrofit.Api.StudentApi;
import com.example.teachingapp.retrofit.Api.TeacherApi;
import com.example.teachingapp.retrofit.Api.UserApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String type;
    private Settings settings;
    private RetrofitService retrofitService;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        LinearLayout settingsBar = findViewById(R.id.settingsBar);
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        Button settingsButton = findViewById(R.id.settings_buttton);
        Switch leftHandSwitch = findViewById(R.id.lef_hand_switch);
        settings = new Settings(settingsButton, settingsBar, sharedPreferences, leftHandSwitch, this);
    }


    public void checkLoginData(View view){
        EditText loginText = findViewById(R.id.LoginText);
        EditText passwordText = findViewById(R.id.PasswordText);
        Button loginButton = findViewById(R.id.button);

        retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        String login = loginText.getText().toString();
        String password = passwordText.getText().toString();
        Log.d("info", login + " " + password);

        userApi.getUserByMail(login, password)
                .enqueue(new Callback<UserDTO>() {
                    @Override
                    public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                            tryLogin(response.body());
                    }

                    @Override
                    public void onFailure(Call<UserDTO> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }

    private void tryLogin(UserDTO user) {
        if (user.getId() == null) {
            Toast.makeText(MainActivity.this, "Wrong mail", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.isStudent()) {
            StudentApi studentApi = retrofitService.getRetrofit().create(StudentApi.class);

            studentApi.studentLogin(user.getId()).enqueue(new Callback<StudentDTO>() {
                @Override
                public void onResponse(Call<StudentDTO> call, Response<StudentDTO> response) {
                    goToStudentChooseSubject(response.body());
                }

                @Override
                public void onFailure(Call<StudentDTO> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Nie znaleziono Id użytkownika",
                            Toast.LENGTH_SHORT).show();
                    Log.e("StudentApiError", "Error occurred: " + t.getMessage(), t);
                }
            });

        } else if (!user.isStudent()) {

            TeacherApi teacherApi = retrofitService.getRetrofit().create(TeacherApi.class);
            teacherApi.teacherLogin(user.getId()).enqueue(new Callback<TeacherDTO>() {
                @Override
                public void onResponse(Call<TeacherDTO> call, Response<TeacherDTO> response) {
                    goToTeacherChooseGroup(response.body());
                }

                @Override
                public void onFailure(Call<TeacherDTO> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Nie znaleziono Id użytkownika",
                            Toast.LENGTH_SHORT).show();
                    Log.e("TeacherApiError", "Error occurred: " + t.getMessage(), t);
                    }
            });
        }
        else {
            Toast.makeText(MainActivity.this, "Błąd logowania, spróbuj ponownie",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void goToTeacherChooseGroup(TeacherDTO teacherDTO) {
        Intent intent = new Intent(this, ChooseGroup.class);
        intent.putExtra("teacher_id", teacherDTO.getId());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        editor.putString("lessons", gson.toJson(teacherDTO.getLessons()));
        editor.apply();
        startActivity(intent);
    }

    private void goToStudentChooseSubject(StudentDTO studentDTO) {
        Intent intent = new Intent(this, ChooseSubject.class);
        intent.putExtra("student_id", studentDTO.getId());
        intent.putExtra("nick", studentDTO.getNick());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        editor.putString("groups", gson.toJson(studentDTO.getGroups()));
        editor.apply();
        startActivity(intent);
    }

    public void showForm(View view) {
        Intent intent = new Intent(this, RegistrationForm.class);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }


}