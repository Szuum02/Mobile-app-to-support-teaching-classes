package com.example.teachingapp.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher._TeacherRegistration;

public class _StudentRegistrationName extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._registration_name_student);
        Intent intent = getIntent();
        if (intent != null) {
            sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        }
    }

    public void nextHandler(View view) {
        EditText nameText = findViewById(R.id.name);
        EditText lastNameText = findViewById(R.id.lastName);
        EditText nickText = findViewById(R.id.nick);

        String name = nameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String nick = nickText.getText().toString();

        Intent intent = new Intent(this, _StudentRegistration.class);
        intent.putExtra("name", name);
        intent.putExtra("lastName", lastName);
        intent.putExtra("nick", nick);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }
}
