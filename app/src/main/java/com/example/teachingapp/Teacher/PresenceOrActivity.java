package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;

public class PresenceOrActivity extends AppCompatActivity {
    private Long lessonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence_or);
        Log.d("Presenceor", "dziala");
        Intent intent = getIntent();
        if (intent != null) {
            lessonId = intent.getLongExtra("lesson", 0);  //Todo dodać obsługę wyjątku na brak grupy
            Toast errorToast = Toast.makeText(PresenceOrActivity.this,
                    String.valueOf(lessonId), Toast.LENGTH_SHORT);                               // dla ułatwienia, usunąć przed pokazaniem
            errorToast.show();
        }

        Button activityButton = findViewById(R.id.activityButton);
        Button presenceButton = findViewById(R.id.presenceButton);

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(v, lessonId);
            }
        });

        presenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPresence(v, lessonId);
            }
        });
    }

    public void goToPresence(View view, long lessonId){
        Intent intent = new Intent(this, CheckPresence.class);
        intent.putExtra("lesson", lessonId);
        startActivity(intent);
    }

    public void goToActivity(View view, long lessonId){
        Intent intent = new Intent(this, CheckActivity.class);
        intent.putExtra("lesson", lessonId);
        startActivity(intent);
    }
}
