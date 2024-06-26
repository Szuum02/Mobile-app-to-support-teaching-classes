package com.example.teaching_app.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.teaching_app.R;
import com.example.teaching_app.Tasks.StudentsActivityTask;

public class CheckActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Long lessonId;
        Intent intent = getIntent();
        if (intent != null) {
            lessonId = intent.getLongExtra("lesson", 0);  //Todo dodać obsługę wyjątku na brak grupy
            Toast errorToast = Toast.makeText(CheckActivity.this,
                    "wita aktywność: " + lessonId, Toast.LENGTH_SHORT);                               // dla ułatwienia, usunąć przed pokazaniem
            errorToast.show();

            StudentsActivityTask studentsActivityTask = new StudentsActivityTask(CheckActivity.this, lessonId);
            studentsActivityTask.findAndShowStudents();
        }


    }

}