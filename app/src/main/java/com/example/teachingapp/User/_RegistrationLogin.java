package com.example.teachingapp.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Student._StudentRegistration;
import com.example.teachingapp.Teacher._TeacherRegistration;
import com.example.teachingapp.retrofit.Api.UserApi;
import com.example.teachingapp.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class _RegistrationLogin extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private final RetrofitService retrofitService = new RetrofitService();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._registration_form_login);
        Intent intent = getIntent();
        if (intent != null) {
            sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        }
    }

    public void nextHandler(View view) {
        EditText mailText = findViewById(R.id.login);
        EditText passwordText = findViewById(R.id.password);
        EditText confirmPasswordText = findViewById(R.id.confirmPassword);

        String mail = mailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if (!password.equals(confirmPassword)) {
            confirmPasswordText.setError("Podane hasła są różne");
            return;
        }

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        userApi.checkUniqueMail(mail).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()) {
                    insertUser(mail, password, type.equals("student"));
                    return;
                }
                mailText.setError("Podany mail już istnieje");
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public void insertUser(String mail, String password, boolean isStudent) {
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        userApi.addUser(mail, password, isStudent).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (isStudent) {
                    goToStudentRegistration(response.body());
                    return;
                }
                goToTeacherRegistration(response.body());

            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                // TODO -> handle error
            }
        });
    }

    private void goToStudentRegistration(long id) {
        Intent intent = new Intent(this, _StudentRegistration.class);
        intent.putExtra("id", id);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }

    private void goToTeacherRegistration(long id) {
        Intent intent = new Intent(this, _TeacherRegistration.class);
        intent.putExtra("id", id);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }

    public void returnHandler(View view) {
        Intent intent = new Intent(this, _RegistrationForm.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }
}
