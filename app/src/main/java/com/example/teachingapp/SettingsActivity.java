package com.example.teachingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.Tasks.MainPageTeacherTask;
import com.example.teachingapp.Tasks.SettingsTask;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._settings_teacher);
        Intent intent = getIntent();
        if (intent != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            SettingsTask settingsTask = new SettingsTask(this, sharedPreferences);
            settingsTask.startTask();
        }
    }
}
