package com.example.teachingapp.Teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;

public class ChooseAction extends AppCompatActivity {
    private Long groupId;
    private Long lessonId;
    private String groupCode;
    private Button lessonButton;
    private Button statsButton;
    private TextView groupCodeTextView;
    private Button returnButton;
    private Context activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._choose_action_teacher);
        Intent intent = getIntent();
        if (intent != null) {
            groupId = intent.getLongExtra("group_id", 0);
            lessonId = intent.getLongExtra("lesson_id", 0);
            groupCode = intent.getStringExtra("group_code");
        }

        setUpLessonButton();
        setUpStatsButton();
        setUpGroupCodeText();
        setUpReturnButton();
    }

    public void setUpLessonButton() {
        lessonButton = findViewById(R.id.no_button);
        lessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CheckPresence.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("lesson_id", lessonId);
                intent.putExtra("group_code", groupCode);
                startActivity(intent);
            }
        });
    }

    public void setUpStatsButton() {
        statsButton = findViewById(R.id.yes_button);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChooseStudent.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("lesson_id", lessonId);
                intent.putExtra("group_code", groupCode);
                startActivity(intent);
            }
        });
    }

    public void setUpGroupCodeText() {
        groupCodeTextView = findViewById(R.id.group_code_textView);
        groupCodeTextView.setText("Kod grupy: " + groupCode);
    }

    public void setUpReturnButton() {
        returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AllGroups.class);
                activity.startActivity(intent);
            }
        });
    }
}
