package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;

public class PresenceOrActivity extends AppCompatActivity {
    private Long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence_or);
        Log.d("Presenceor", "dziala");
        Intent intent = getIntent();
        if (intent != null) {
            groupId = intent.getLongExtra("group_id", 0);  //Todo dodać obsługę wyjątku na brak grupy
        }

        Button activityButton = findViewById(R.id.activityButton);
        Button presenceButton = findViewById(R.id.presenceButton);

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(v, groupId);
            }
        });

        presenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPresence(v, groupId);
            }
        });
    }

    public void goToPresence(View view, long lessonId){
        Intent intent = new Intent(this, CheckPresence.class);
        intent.putExtra("group_id", lessonId);
        startActivity(intent);
    }

    public void goToActivity(View view, long lessonId){
        Intent intent = new Intent(this, CheckActivity.class);
        intent.putExtra("group_id", lessonId);
        startActivity(intent);
    }
}
