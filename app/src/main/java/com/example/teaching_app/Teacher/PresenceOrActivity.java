package com.example.teaching_app.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.teaching_app.R;

public class PresenceOrActivity extends AppCompatActivity {
    Integer group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence_or);
        Log.d("Presenceor", "dziala");
        Intent intent = getIntent();
        if (intent != null) {
            group = intent.getIntExtra("group", 0);  //Todo dodać obsługę wyjątku na brak grupy
            Toast errorToast = Toast.makeText(PresenceOrActivity.this,
                    String.valueOf(group), Toast.LENGTH_SHORT);                               // dla ułatwienia, usunąć przed pokazaniem
            errorToast.show();
        }

        Button activityButton = findViewById(R.id.activityButton);
        Button presenceButton = findViewById(R.id.presenceButton);

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(v, group);
            }
        });

        presenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPresence(v, group);
            }
        });
    }

    public void goToPresence(View view, int group){
        Intent intent = new Intent(this, CheckPresence.class);
        intent.putExtra("group", group);
        startActivity(intent);
    }

    public void goToActivity(View view, int group){
        Intent intent = new Intent(this, CheckActivity.class);
        intent.putExtra("group", group);
        startActivity(intent);
    }
}