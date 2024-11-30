package com.example.teachingapp.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.MainActivity;
import com.example.teachingapp.R;
import com.example.teachingapp.Student.ChooseSubject;
import com.example.teachingapp.Student.StudentMainPage;
import com.example.teachingapp.Teacher.TeacherMainPage;
import com.example.teachingapp.dtos.StudentDTO;
import com.example.teachingapp.dtos.TeacherDTO;
import com.example.teachingapp.dtos.UserDTO;
import com.example.teachingapp.retrofit.Api.StudentApi;
import com.example.teachingapp.retrofit.Api.TeacherApi;
import com.example.teachingapp.retrofit.Api.UserApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class _Login extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private final RetrofitService retrofitService = new RetrofitService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._login_view);
        Intent intent = getIntent();
        if (intent != null) {
            sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        }
    }

    public void loginHandler(View view) {
        EditText loginText = findViewById(R.id.mail);
        EditText passwordText = findViewById(R.id.password);

        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        String login = loginText.getText().toString();
        String password = passwordText.getText().toString();

        userApi.getUserByMail(login, password)
                .enqueue(new Callback<UserDTO>() {
                    @Override
                    public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                        if (response.body().getId() == -1) {
                            passwordText.setError("Błędny login lub hasło");
                            return;
                        }
                        tryLogin(response.body());
                    }

                    @Override
                    public void onFailure(Call<UserDTO> call, Throwable t) {
                        Toast.makeText(_Login.this, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(_Login.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }

    public void returnHandler(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }

    private void tryLogin(UserDTO user) {
        if (user.getId() == null) {
            Toast.makeText(_Login.this, "Wrong mail", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.isStudent()) {
            StudentApi studentApi = retrofitService.getRetrofit().create(StudentApi.class);

            studentApi.studentLogin(user.getId()).enqueue(new Callback<StudentDTO>() {
                @Override
                public void onResponse(Call<StudentDTO> call, Response<StudentDTO> response) {
                    goToStudentMainPage(response.body());
                }

                @Override
                public void onFailure(Call<StudentDTO> call, Throwable t) {
                    Toast.makeText( _Login.this, "Nie znaleziono Id użytkownika",
                            Toast.LENGTH_SHORT).show();
                    Log.e("StudentApiError", "Error occurred: " + t.getMessage(), t);
                }
            });

        } else if (!user.isStudent()) {
            TeacherApi teacherApi = retrofitService.getRetrofit().create(TeacherApi.class);
            teacherApi.teacherLogin(user.getId()).enqueue(new Callback<TeacherDTO>() {
                @Override
                public void onResponse(Call<TeacherDTO> call, Response<TeacherDTO> response) {
                    goToTeacherMainPage(response.body());
                }

                @Override
                public void onFailure(Call<TeacherDTO> call, Throwable t) {
                    Toast.makeText(_Login.this, "Nie znaleziono Id użytkownika",
                            Toast.LENGTH_SHORT).show();
                    Log.e("TeacherApiError", "Error occurred: " + t.getMessage(), t);
                }
            });
        }
        else {
            Toast.makeText(_Login.this, "Błąd logowania, spróbuj ponownie",
                    Toast.LENGTH_SHORT).show();
        }

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

    private void goToStudentMainPage(StudentDTO studentDTO) {
        Intent intent = new Intent(this, StudentMainPage.class);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        intent.putExtra("student_id", studentDTO.getId());
        intent.putExtra("nick", studentDTO.getNick());
        editor.putString("lessons", gson.toJson(studentDTO.getLessons()));
        editor.putString("student_data", gson.toJson(studentDTO));
        editor.apply();
        startActivity(intent);
    }
}
