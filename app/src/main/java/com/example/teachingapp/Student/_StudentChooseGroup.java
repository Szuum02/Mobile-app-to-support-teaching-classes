package com.example.teachingapp.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.GroupsTask;
import com.example.teachingapp.Tasks.StudentGroupsTask;

public class _StudentChooseGroup extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._all_student_subjects_page);
        Intent intent = getIntent();
        if (intent != null) {

            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            StudentGroupsTask groupsTask = new StudentGroupsTask(this, sharedPreferences);
            groupsTask.startTask();
        }
    }
}
