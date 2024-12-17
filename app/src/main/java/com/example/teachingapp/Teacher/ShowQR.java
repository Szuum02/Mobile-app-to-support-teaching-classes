package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.TeacherShowQRTask;

public class ShowQR extends AppCompatActivity {
    private long groupId;
    private long lessonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout._show_qr_code_teacher);

        if (intent != null) {
            groupId = intent.getLongExtra("group_id", 0);
            lessonId = intent.getLongExtra("lesson_id", 0);

            TeacherShowQRTask teacherShowQRTask = new TeacherShowQRTask(ShowQR.this, groupId, lessonId);
            teacherShowQRTask.startTask();
        }
    }
}
