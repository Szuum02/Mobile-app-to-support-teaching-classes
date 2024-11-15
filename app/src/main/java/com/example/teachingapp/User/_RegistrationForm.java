package com.example.teachingapp.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.ShowActivity;
import com.example.teachingapp.Student._StudentRegistration;
import com.example.teachingapp.Teacher._TeacherRegistration;

public class _RegistrationForm extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._registration_type);
        Intent intent = getIntent();
        if (intent != null) {
            sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        }
    }

    public void teacherHandler(View view) {
        Intent intent = new Intent(this, _RegistrationLogin.class);
        intent.putExtra("type", "teacher");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }

    public void studentHandler(View view) {
        Intent intent = new Intent(this, _RegistrationLogin.class);
        intent.putExtra("type", "student");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }
}
