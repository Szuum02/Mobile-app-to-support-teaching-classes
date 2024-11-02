package com.example.teachingapp.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.ShowPresenceTask;

public class ShowPresence extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_check_presence);
        long studentId;
        long groupId;
        String subject;
        String nick;
        Intent intent = getIntent();
        if (intent != null) {
            studentId = intent.getLongExtra("student_id", 0);
            groupId = intent.getLongExtra("group_id", 0);
            subject = intent.getStringExtra("subject");
            nick = intent.getStringExtra("nick");

            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

            ShowPresenceTask presenceTask = new ShowPresenceTask(this, groupId, studentId, subject, nick, sharedPreferences);
            presenceTask.findAndShowPresences();
        }

    }
}
