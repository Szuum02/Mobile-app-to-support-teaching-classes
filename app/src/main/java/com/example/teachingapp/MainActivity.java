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

//import com.example.teachingapp.DNS.MdnsServiceDiscovery;
import com.example.teachingapp.Student.ChooseSubject;
import com.example.teachingapp.Teacher.TeacherMainPage;
import com.example.teachingapp.User.RegistrationForm;
import com.example.teachingapp.User._Login;
import com.example.teachingapp.User._RegistrationForm;
import com.example.teachingapp.User._RegistrationName;
import com.example.teachingapp.dtos.StudentDTO;
import com.example.teachingapp.dtos.TeacherDTO;
import com.example.teachingapp.dtos.UserDTO;
import com.example.teachingapp.retrofit.Api.StudentApi;
import com.example.teachingapp.retrofit.Api.TeacherApi;
import com.example.teachingapp.retrofit.Api.UserApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.io.IOException;
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
        setContentView(R.layout._welcome_view);
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

    }

    public void loginHandler(View view) {
        Intent intent = new Intent(this, _Login.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }

    public void registerHandler(View view) {
        Intent intent = new Intent(this, _RegistrationForm.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }

}