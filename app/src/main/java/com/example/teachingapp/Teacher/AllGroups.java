package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.GroupsTask;
import com.example.teachingapp.Tasks.MainPageTeacherTask;

public class AllGroups extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);
        Intent intent = getIntent();
        if (intent != null) {

            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            GroupsTask groupsTask = new GroupsTask(this, sharedPreferences);
            groupsTask.startTask();
        }
    }
}
