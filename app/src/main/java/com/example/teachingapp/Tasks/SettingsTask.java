package com.example.teachingapp.Tasks;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.teachingapp.R;
import com.example.teachingapp.SettingsActivity;

public class SettingsTask {
    private SettingsActivity activity;
    private SharedPreferences sharedPreferences;
    private Button noButton;
    private Button yesButton;
    private Button returnButton;

    public SettingsTask(SettingsActivity activity, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
        this.noButton = activity.findViewById(R.id.lesson_button);
        this.yesButton = activity.findViewById(R.id.stats_button);
        this.returnButton = activity.findViewById(R.id.return_button);
    }

    public void startTask() {
        setUpNoButton();
        setUpYesButton();
        setUpReturnButton();
    }

    private void setUpNoButton() {
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("left_hand", "off");
                editor.apply();

                Toast.makeText(activity, "Tryb leworęczny został wyłączony", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpYesButton() {
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("left_hand", "on");
                editor.apply();

                Toast.makeText(activity, "Tryb leworęczny został włączony", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setUpReturnButton() {
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }
}
