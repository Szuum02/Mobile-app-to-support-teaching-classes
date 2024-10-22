package com.example.teachingapp.Student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.ChooseSubjectTask;

public class ChooseSubject extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_choose_group);
        long studentId;
        Intent intent = getIntent();
        if (intent != null) {
            studentId = intent.getLongExtra("student_id", 0);
            Toast errorToast = Toast.makeText(ChooseSubject.this,
                    "wita obecność: " + studentId, Toast.LENGTH_SHORT);
            errorToast.show();

            LinearLayout layout = findViewById(R.id.linearLayout);

            ChooseSubjectTask subjectTask = new ChooseSubjectTask(this, studentId);
            subjectTask.findAndShowGroups();
        }

    }
}
