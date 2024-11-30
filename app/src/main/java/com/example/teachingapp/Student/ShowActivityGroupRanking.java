package com.example.teachingapp.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.RankingActivityTask;

public class ShowActivityGroupRanking extends ShowActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._student_group_activity);
        Intent intent = getIntent();
        if (intent != null) {
            long studentId = intent.getLongExtra("student_id", 0);
            long groupId = intent.getLongExtra("group_id", 0);
            String nick = intent.getStringExtra("nick");
            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

            RankingActivityTask rankingTask = new RankingActivityTask(this, groupId, studentId, nick, sharedPreferences);
            rankingTask.getGroupRanking();
        }

    }
}
