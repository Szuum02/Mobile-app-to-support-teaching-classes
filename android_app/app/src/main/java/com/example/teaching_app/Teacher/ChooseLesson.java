package com.example.teaching_app.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teaching_app.R;
import com.example.teaching_app.Tasks.GroupsTask;
import com.example.teaching_app.Tasks.LessonTask;

public class ChooseLesson extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lesson);
        long groupId;
        Intent intent = getIntent();
        if (intent != null) {
            groupId = intent.getLongExtra("group", 0);
            Toast errorToast = Toast.makeText(ChooseLesson.this,
                    "wita obecność: " + groupId, Toast.LENGTH_SHORT);
            errorToast.show();

            LinearLayout layout = findViewById(R.id.linearLayout);

            LessonTask lessonTask = new LessonTask(this, groupId);
            lessonTask.findAndShowLessons();
        }
    }
}
