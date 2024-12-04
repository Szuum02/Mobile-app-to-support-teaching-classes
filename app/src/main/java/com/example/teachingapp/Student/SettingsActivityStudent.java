package com.example.teachingapp.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.SettingsTask;
import com.example.teachingapp.Tasks.SettingsTaskStudent;

public class SettingsActivityStudent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._settings_student);
        Intent intent = getIntent();
        if (intent != null) {
            Long studentId = intent.getLongExtra("student_id", 0);
            SettingsTaskStudent settingsTask = new SettingsTaskStudent(this, studentId);
            settingsTask.startTask();
        }
    }
}
