package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.StudentsPresenceTask;

public class CheckPresence extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        if(sharedPreferences.getString("left_hand", "off").equals("on")){
            setContentView(R.layout._left_hand_presence_teacher);
        } else {
            setContentView(R.layout._right_hand_presence_teacher);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Long groupId = intent.getLongExtra("group_id", 0);
            Long lessonId = intent.getLongExtra("lesson_id", 0);
            String groupCode = intent.getStringExtra("group_code");
            StudentsPresenceTask studentsPresenceTask = new StudentsPresenceTask(CheckPresence.this, groupId, lessonId, groupCode, sharedPreferences);
            studentsPresenceTask.startTask();
        }

    }
}
