package com.example.teachingapp.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.StudentShowQRTask;
import com.example.teachingapp.Tasks.TeacherShowQRTask;
import com.example.teachingapp.Teacher.ShowQR;

public class StudentShowQR extends AppCompatActivity {
    private long studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout._student_qr_code);
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

        if (intent != null) {
            studentId = intent.getLongExtra("student_id", 0);

            StudentShowQRTask studentShowQRTask = new StudentShowQRTask(this, studentId, sharedPreferences);
            studentShowQRTask.startTask();
        }
    }
}
