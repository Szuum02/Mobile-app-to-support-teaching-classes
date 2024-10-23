package com.example.teachingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.Student.ChooseSubject;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.models.User;
import com.example.teachingapp.retrofit.Api.UserApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
    }


    public void checkLoginData(View view){
        EditText loginText = findViewById(R.id.LoginText);
        EditText passwordText = findViewById(R.id.PasswordText);
        Button loginButton = findViewById(R.id.button);

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        String login = loginText.getText().toString();
        String password = passwordText.getText().toString();
        Log.d("info", login + " " + password);

        userApi.getUserByMail(login, password)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        tryLogin(response.body(), password);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }

    private void tryLogin(User user, String password) {
        if (user == null) {
            Toast.makeText(MainActivity.this, "Wrong mail", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!user.getPassword().equals(password)) {
            Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user.isStudent()) {
            Intent intent = new Intent(this, ChooseSubject.class);
            intent.putExtra("student_id", user.getId());
            this.startActivity(intent);
            return;
        }

        Intent intent = new Intent(this, ChooseGroup.class);
        intent.putExtra("teacher_id", 27);
        startActivity(intent);
    }
}