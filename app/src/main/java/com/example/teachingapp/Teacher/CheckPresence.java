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
        Long lessonId;

        Intent intent = getIntent();
        if (intent != null) {
            lessonId = intent.getLongExtra("lesson", 0);  //Todo dodać obsługę wyjątku na brak grupy
            Toast errorToast = Toast.makeText(CheckPresence.this,
                    "wita obecność: " + lessonId, Toast.LENGTH_SHORT);
            errorToast.show();

            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

            StudentsPresenceTask studentsPresenceTask = new StudentsPresenceTask(CheckPresence.this, lessonId, sharedPreferences);
            studentsPresenceTask.findAndShowStudents();
        }

    }
}
