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
        setContentView(R.layout._student_attendance);
        Intent intent = getIntent();
        if (intent != null) {
            long studentId = intent.getLongExtra("student_id", 0);
            long groupId = intent.getLongExtra("group_id", 0);
//          String  subject = intent.getStringExtra("subject");
            String nick = intent.getStringExtra("nick");

            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

            ShowPresenceTask presenceTask = new ShowPresenceTask(this, groupId, studentId, nick, sharedPreferences);
            presenceTask.findAndShowPresences();
        }

    }
}
