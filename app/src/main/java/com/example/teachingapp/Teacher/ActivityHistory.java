package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.ActivityHistoryTask;
import com.example.teachingapp.Tasks.StudentsActivityTask;

public class ActivityHistory extends AppCompatActivity {
    Long studentId;
    Long groupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_history);

        if (intent != null) {
            studentId = intent.getLongExtra("studentId", 0);  //Todo dodać obsługę wyjątku na brak grupy
            groupId = intent.getLongExtra("group_id", 0);  //Todo dodać obsługę wyjątku na brak grupy

            ActivityHistoryTask activityHistoryTask = new ActivityHistoryTask(ActivityHistory.this, studentId, groupId);
            activityHistoryTask.showHistory();
        }
    }
}
