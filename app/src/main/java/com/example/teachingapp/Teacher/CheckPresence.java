package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.StudentsPresenceTask;

public class CheckPresence extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_presence);
        Long groupId;

        Intent intent = getIntent();
        if (intent != null) {
            groupId = intent.getLongExtra("group_id", 0);  //Todo dodać obsługę wyjątku na brak grupy

            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

            StudentsPresenceTask studentsPresenceTask = new StudentsPresenceTask(CheckPresence.this, groupId, sharedPreferences);
            studentsPresenceTask.findAndShowStudents();
        }

    }
}
