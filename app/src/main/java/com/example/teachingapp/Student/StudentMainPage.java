package com.example.teachingapp.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.MainPageStudentTask;
import com.example.teachingapp.Tasks.MainPageTeacherTask;

public class StudentMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._main_page_student);
        Intent intent = getIntent();
        if (intent != null) {

            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            MainPageStudentTask mainPageStudentTask = new MainPageStudentTask(this, sharedPreferences);
            mainPageStudentTask.startTask();
        }
    }
}
