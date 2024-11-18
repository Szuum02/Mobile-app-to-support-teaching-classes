package com.example.teachingapp.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.GroupsTask;
import com.example.teachingapp.Tasks.MainPageTeacherTask;

public class TeacherMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._main_page_teacher);
        Intent intent = getIntent();
        if (intent != null) {

            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            MainPageTeacherTask mainPageTeacherTask = new MainPageTeacherTask(this, sharedPreferences);
            mainPageTeacherTask.startTask();
        }
    }
}
