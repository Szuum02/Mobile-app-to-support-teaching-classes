package com.example.teachingapp.Tasks;

import android.content.SharedPreferences;

import com.example.teachingapp.SettingsActivity;
import com.example.teachingapp.Teacher.TeacherMainPage;

public class SettingsTask {
    private SettingsActivity activity;
    private SharedPreferences sharedPreferences;

    public SettingsTask(SettingsActivity activity, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
    }

    public void startTask() {

    }
}
