package com.example.teachingapp.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.ChooseSubjectTask;

public class ChooseSubject extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_choose_group);
        Intent intent = getIntent();
        if (intent != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            ChooseSubjectTask subjectTask = new ChooseSubjectTask(this, sharedPreferences);
            subjectTask.findAndShowSubjects();
        }

    }
}
