package com.example.teaching_app.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teaching_app.R;

import java.util.ArrayList;

public class CheckPresence extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_presence);
        String group;

        Intent intent = getIntent();
        if (intent != null) {
            group = intent.getStringExtra("group");  //Todo dodać obsługę wyjątku na brak grupy
            Toast errorToast = Toast.makeText(CheckPresence.this,
                    "wita obecność: " + group, Toast.LENGTH_SHORT);
            errorToast.show();
        }

        LinearLayout dynamicLayout = findViewById(R.id.dynamic_layout);

        int numberOfSets = 20;
        ArrayList<TextView> textViewList = new ArrayList<>();

        for (int i = 0; i < numberOfSets; i++) {

            // poziom przycisków
            LinearLayout layout = new LinearLayout(this);
            layout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setPadding(0, 10, 0, 10);

            // Tworzenie miejsca na imie i nazwisko
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            textView.setText("asia" + i );
            layout.addView(textView);
            textViewList.add(textView);

            // Tworzenie przycisków
            addPresenceButton(textViewList, layout, i);
            addAbsenceButton(textViewList, layout, i);
            addLateButton(textViewList, layout, i);

            // Dodanie zestawu do głównego layoutu
            dynamicLayout.addView(layout);
        }
    }


    public void addPresenceButton(ArrayList<TextView> textViewList, LinearLayout layout, int i){
        Button button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("Ob");
        final int index = i;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckPresence.this,
                        textViewList.get(index).getText().toString() + " Ob", Toast.LENGTH_SHORT).show();
            }
        });
        layout.addView(button);
    }

    public void addAbsenceButton(ArrayList<TextView> textViewList, LinearLayout layout, int i){
        Button button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("Nb");
        final int index = i;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckPresence.this,
                        textViewList.get(index).getText().toString() + " Nb", Toast.LENGTH_SHORT).show();
            }
        });
        layout.addView(button);
    }

    public void addLateButton(ArrayList<TextView> textViewList, LinearLayout layout, int i){
        Button button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("Sp");
        final int index = i;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckPresence.this,
                        textViewList.get(index).getText().toString() + " Sp", Toast.LENGTH_SHORT).show();
            }
        });
        layout.addView(button);
    }
}