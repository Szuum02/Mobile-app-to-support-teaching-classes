package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.ActivityHistoryTask;

public class ActivityHistory extends AppCompatActivity {
    Long studentId;
    Long groupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout._choosed_student_activity);

        if (intent != null) {
            studentId = intent.getLongExtra("student_id", 0);
            groupId = intent.getLongExtra("group_id", 0);

            ActivityHistoryTask activityHistoryTask = new ActivityHistoryTask(ActivityHistory.this, studentId, groupId);
            activityHistoryTask.startTask();
        }
    }
}
