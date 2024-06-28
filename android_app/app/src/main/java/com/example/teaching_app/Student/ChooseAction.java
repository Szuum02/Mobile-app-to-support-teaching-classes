package com.example.teaching_app.Student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teaching_app.R;
import com.example.teaching_app.Teacher.CheckActivity;
import com.example.teaching_app.Teacher.CheckPresence;

public class ChooseAction extends AppCompatActivity {
    private long groupId;
    private long studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_choose_action);
        Log.d("Presenceor", "dziala");
        Intent intent = getIntent();
        if (intent != null) {
            groupId = intent.getLongExtra("group", 0);  //Todo dodać obsługę wyjątku na brak grupy
            studentId = intent.getLongExtra("student", 0);
            Toast errorToast = Toast.makeText(ChooseAction.this,
                    String.valueOf(groupId), Toast.LENGTH_SHORT);                               // dla ułatwienia, usunąć przed pokazaniem
            errorToast.show();
        }

        Button activityButton = findViewById(R.id.activityButton);
        Button presenceButton = findViewById(R.id.presenceButton);

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(v, groupId, studentId);
            }
        });

        presenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPresence(v, groupId, studentId);
            }
        });
    }

    public void goToPresence(View view, long groupId, long studentId){
        Intent intent = new Intent(this, CheckPresence.class);
        intent.putExtra("lesson", groupId);
        startActivity(intent);
    }

    public void goToActivity(View view, long groupId, long studentId){
        Intent intent = new Intent(this, CheckActivity.class);
        intent.putExtra("lesson", groupId);
        startActivity(intent);
    }
}
