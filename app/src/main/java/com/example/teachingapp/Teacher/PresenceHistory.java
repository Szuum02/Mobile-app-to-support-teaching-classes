package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.ActivityHistoryTask;
import com.example.teachingapp.Tasks.PresenceHistoryTeacherTask;

public class PresenceHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout._choosed_student_presence_teacher);
        Long groupId = intent.getLongExtra("group_id", 0);
        Long studentId = intent.getLongExtra("student_id", 0);
        String groupCode = intent.getStringExtra("group_code");

        PresenceHistoryTeacherTask presenceHistoryTeacherTask = new PresenceHistoryTeacherTask(this, groupId, studentId, groupCode);
        presenceHistoryTeacherTask.startTask();
    }
}
