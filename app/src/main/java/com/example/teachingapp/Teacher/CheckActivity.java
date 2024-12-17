package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.StudentsActivityTask;

public class CheckActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        if(sharedPreferences.getString("left_hand", "off").equals("on")){
            setContentView(R.layout._left_hand_activity_teacher);
        } else {
            setContentView(R.layout._right_hand_activity_teacher);
        }

        Intent intent = getIntent();

        if (intent != null) {
            Long groupId = intent.getLongExtra("group_id", 0);
            Long lessonId = intent.getLongExtra("lesson_id", 0);
            StudentsActivityTask studentsActivityTask = new StudentsActivityTask(CheckActivity.this, groupId, lessonId, sharedPreferences);
            studentsActivityTask.startTask();
        }

    }


}
