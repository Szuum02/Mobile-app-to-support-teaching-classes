package com.example.teachingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.teachingapp.User._Login;
import com.example.teachingapp.User._RegistrationForm;

public class MainActivity extends AppCompatActivity {
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