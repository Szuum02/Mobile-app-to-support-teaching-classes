package com.example.teaching_app.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.teaching_app.R;

public class ChooseGroup extends AppCompatActivity {

    private static int groupsNumber = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);

        LinearLayout layout = findViewById(R.id.linearLayout);

        for(int i = 0; i < groupsNumber; i++){
            Button button = new Button(this);
            button.setText("grupa " + Integer.toString(i));

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String group = button.getText().toString();
                    goToChosenGroup(group);
                }
            });
            layout.addView(button); // Dodaj przycisk do kontenera
        }
    }

    private void goToChosenGroup(String group){
        Intent intent = new Intent(this, PresenceOrActivity.class);
        intent.putExtra("group", group);
        startActivity(intent);
    }
}

