package com.example.teachingapp.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.GroupsTask;

public class ChooseGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);
        Intent intent = getIntent();
        if (intent != null) {

            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            GroupsTask groupsTask = new GroupsTask(this, sharedPreferences);
            groupsTask.findAndShowGroups();
        }
    }
}
