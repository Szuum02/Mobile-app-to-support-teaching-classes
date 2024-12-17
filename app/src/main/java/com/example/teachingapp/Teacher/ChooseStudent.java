package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.ChooseStudentTask;

public class ChooseStudent extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._choose_student_teacher);
        Intent intent = getIntent();

        if (intent != null) {
            Long groupId = intent.getLongExtra("group_id", 0);
            Long lessonId = intent.getLongExtra("lesson_id", 0);
            String groupCode = intent.getStringExtra("group_code");
            ChooseStudentTask chooseStudentTask = new ChooseStudentTask(ChooseStudent.this, groupId, lessonId, groupCode);
            chooseStudentTask.startTask();
        }

    }

    }