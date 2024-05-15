package com.example.teaching_app.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teaching_app.R;

import java.util.ArrayList;

public class CheckActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Integer group;
        Intent intent = getIntent();
        if (intent != null) {
            group = intent.getIntExtra("group",0);  //Todo dodać obsługę wyjątku na brak grupy
            Toast errorToast = Toast.makeText(CheckActivity.this,
                    "wita aktywność: " + group, Toast.LENGTH_SHORT);                               // dla ułatwienia, usunąć przed pokazaniem
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
            addPointsButton(textViewList, layout, i, 1);
            addPointsButton(textViewList, layout, i, -1);


            // Dodanie zestawu do głównego layoutu
            dynamicLayout.addView(layout);
        }
    }

    public void addPointsButton(ArrayList<TextView> textViewList, LinearLayout layout, int i, int val){
        Button button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        String text = (val == 1) ? "+1" : "-1";
        button.setText(text);
        final int index = i;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckActivity.this,
                        textViewList.get(index).getText().toString() + " : "
                                + button.getText().toString() , Toast.LENGTH_SHORT).show();
            }
        });
        layout.addView(button);
    }
}