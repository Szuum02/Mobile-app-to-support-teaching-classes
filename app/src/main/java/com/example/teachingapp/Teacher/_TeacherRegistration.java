package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.User._RegistrationName;
import com.example.teachingapp.dtos.TeacherDTO;
import com.example.teachingapp.retrofit.Api.UserApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class _TeacherRegistration extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String name;
    private String lastName;
    private RetrofitService retrofitService;

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

    public void returnHandler(View view) {
        Intent intent = new Intent(this, _RegistrationName.class);
        intent.putExtra("name", name);
        intent.putExtra("lastName", lastName);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }

    private void checkDataAndRegister(EditText mailText, EditText passwordText, EditText confirmPasswordText) {
        String mail = mailText.getText().toString();
        String password = passwordText.getText().toString();

        if (validateEmail(mailText) && validateConfirmPassword(passwordText, confirmPasswordText)) {
            retrofitService = new RetrofitService();
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
    }

    public void registerTeacher(String mail, String password) {
        retrofitService = new RetrofitService();
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

    public boolean validateEmail(EditText loginText) {
        Pattern p = Pattern.compile("^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$");
        Matcher m = p.matcher(loginText.getText().toString());
        if (!m.matches()) {
            loginText.setError("Błędny email");
            return false;
        }
        return true;
    }

    public boolean validateConfirmPassword(EditText passwordText, EditText confirmPasswordText) {
        if (!passwordText.getText().toString().equals(
                confirmPasswordText.getText().toString()
        )) {
            confirmPasswordText.setError("Hasła są różne");
            return false;
        }
        return true;
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
