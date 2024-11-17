package com.example.teachingapp.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Student._StudentRegistration;
import com.example.teachingapp.Teacher._TeacherRegistration;

public class _RegistrationName extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._registration_form_teacher);
        Intent intent = getIntent();
        if (intent != null) {
            sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            type = intent.getStringExtra("type");
        }
    }

    public void nextHandler(View view) {
        EditText nameText = findViewById(R.id.name);
        EditText lastNameText = findViewById(R.id.lastName);

        String name = nameText.getText().toString();
        String lastName = lastNameText.getText().toString();

        if (type.equals("teacher")) {
            Intent intent = new Intent(this, _TeacherRegistration.class);
            intent.putExtra("name", name);
            intent.putExtra("lastName", lastName);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.apply();
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, _StudentRegistration.class);
            intent.putExtra("name", name);
            intent.putExtra("lastName", lastName);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.apply();
            startActivity(intent);
        }
    }
}