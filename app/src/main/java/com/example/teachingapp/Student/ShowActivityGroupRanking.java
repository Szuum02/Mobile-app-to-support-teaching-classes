package com.example.teachingapp.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.RankingActivityTask;

public class ShowActivityGroupRanking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_activity_group_ranking);
        long studentId;
        long groupId;
        String subject;
        Intent intent = getIntent();
        if (intent != null) {
            studentId = intent.getLongExtra("student_id", 0);
            groupId = intent.getLongExtra("group_id", 0);
            subject = intent.getStringExtra("subject");

            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

            RankingActivityTask rankingTask = new RankingActivityTask(this, groupId, studentId, subject, sharedPreferences);
            rankingTask.getGroupRanking();
        }

    }
}
