package com.example.teaching_app.Student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teaching_app.R;
import com.example.teaching_app.Tasks.ShowRankingTask;

public class ShowActivityRanking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_choose_group);
        long studentId;
        long groupId;
        Intent intent = getIntent();
        if (intent != null) {
            studentId = intent.getLongExtra("student", 0);
            groupId = intent.getLongExtra("group", 0);
            Toast errorToast = Toast.makeText(ShowActivityRanking.this,
                    "wita obecność: " + studentId, Toast.LENGTH_SHORT);
            errorToast.show();

            LinearLayout layout = findViewById(R.id.linearLayout);

            ShowRankingTask rankingTask = new ShowRankingTask(this, groupId, studentId);
            rankingTask.showRanking();
        }

    }
}
