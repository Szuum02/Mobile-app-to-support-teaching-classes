package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.StudentsActivityTask;

public class CheckActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        setContentView(R.layout.activity_check_activity);

        Long lessonId;
        Intent intent = getIntent();

        if (intent != null) {
            lessonId = intent.getLongExtra("lesson", 0);  //Todo dodać obsługę wyjątku na brak grupy
            Toast errorToast = Toast.makeText(CheckActivity.this,
                    "wita aktywność: " + lessonId, Toast.LENGTH_SHORT);                               // dla ułatwienia, usunąć przed pokazaniem
            errorToast.show();


            StudentsActivityTask studentsActivityTask = new StudentsActivityTask(CheckActivity.this, lessonId, sharedPreferences);
            studentsActivityTask.findAndShowStudents();
        }



    }

}
