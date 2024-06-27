package com.example.teaching_app.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teaching_app.R;
import com.example.teaching_app.Tasks.StudentsActivityTask;
import com.example.teaching_app.Tasks.StudentsPresenceTask;

import java.util.ArrayList;

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

            StudentsPresenceTask studentsPresenceTask = new StudentsPresenceTask(CheckPresence.this, lessonId);
            studentsPresenceTask.findAndShowStudents();
        }

    }
}