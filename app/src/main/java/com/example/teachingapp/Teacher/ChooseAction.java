package com.example.teachingapp.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;

public class ChooseAction extends AppCompatActivity {
    private Long groupId;
    private Long lessonId;
    private Button lessonButton;
    private Button statsButton;
    private Button returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._choose_action_teacher);
        Intent intent = getIntent();
        if (intent != null) {
            groupId = intent.getLongExtra("group_id", 0);
            lessonId = intent.getLongExtra("lesson_id", 0);
        }

        setUpLessonButton();
        setUpStatsButton();
        setUpReturnButton();
    }

    public void setUpLessonButton() {
        lessonButton = findViewById(R.id.lesson_button);
        lessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CheckPresence.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("lesson_id", lessonId);
                startActivity(intent);
            }
        });
    }

    public void setUpStatsButton() {
        statsButton = findViewById(R.id.stats_button);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChooseStudent.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("lesson_id", lessonId);
                startActivity(intent);
            }
        });
    }

    public void setUpReturnButton() {
        returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();           }
        });
    }
}
