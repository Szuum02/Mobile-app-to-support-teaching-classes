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

        Long groupId;
        Intent intent = getIntent();

        if (intent != null) {
            groupId = intent.getLongExtra("group_id", 0);  //Todo dodać obsługę wyjątku na brak grupy

            StudentsActivityTask studentsActivityTask = new StudentsActivityTask(CheckActivity.this, groupId, sharedPreferences);
            studentsActivityTask.findAndShowStudents();
        }



    }

}
