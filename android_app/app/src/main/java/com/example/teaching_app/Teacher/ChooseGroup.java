package com.example.teaching_app.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.teaching_app.DatabaseClasses.Group;
import com.example.teaching_app.R;
import com.example.teaching_app.Tasks.GroupsTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChooseGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);
        long group;
        Intent intent = getIntent();
        if (intent != null) {
            group = intent.getLongExtra("teacher_id", 0);
            Toast errorToast = Toast.makeText(ChooseGroup.this,
                    "wita obecność: " + group, Toast.LENGTH_SHORT);
            errorToast.show();

            LinearLayout layout = findViewById(R.id.linearLayout);

            GroupsTask groupsTask = new GroupsTask(this, group);
            groupsTask.findAndShowGroups();


        }

    }
}

