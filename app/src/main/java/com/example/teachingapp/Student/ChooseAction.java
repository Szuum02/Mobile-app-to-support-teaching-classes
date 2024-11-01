package com.example.teachingapp.Student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;

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
            groupId = intent.getLongExtra("group_id", 0);  //Todo dodać obsługę wyjątku na brak grupy
            studentId = intent.getLongExtra("student_id", 0);
//            Toast errorToast = Toast.makeText(ChooseAction.this,
//                    String.valueOf(groupId), Toast.LENGTH_SHORT);                               // dla ułatwienia, usunąć przed pokazaniem
//            errorToast.show();
        }
        String subject = intent.getStringExtra("subject");

        TextView subjectText = findViewById(R.id.subject);
        subjectText.setText(subject);

        Button activityButton = findViewById(R.id.activityButton);
        Button presenceButton = findViewById(R.id.presenceButton);
        Button qrButton = findViewById(R.id.qrButton);

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(v, groupId, studentId);
            }
        });

        presenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPresence(v, groupId, studentId, subject);
            }
        });

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO -> generate QR Code
            }
        });
    }

    public void goToPresence(View view, long groupId, long studentId, String subject){
        Intent intent = new Intent(this, ShowPresence.class);
        intent.putExtra("group_id", groupId);
        intent.putExtra("student_id", studentId);
        intent.putExtra("subject", subject);
        startActivity(intent);
    }

    public void goToActivity(View view, long groupId, long studentId){
        Intent intent = new Intent(this, ShowActivityRanking.class);
        intent.putExtra("group_id", groupId);
        intent.putExtra("student_id", studentId);
        startActivity(intent);
    }
}
