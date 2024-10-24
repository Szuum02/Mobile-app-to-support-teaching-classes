package com.example.teachingapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

public class Settings {
    private Button button;
    private LinearLayout settingsBar;
    private SharedPreferences sharedPreferences;
    private Switch leftHandSwitch;
    private String savedLefHandTribe;

    public Settings(Button button, LinearLayout settingsBar, SharedPreferences sharedPreferences, Switch leftHandSwitch) {
        this.button = button;
        this.settingsBar = settingsBar;
        this.sharedPreferences = sharedPreferences;
        this.leftHandSwitch = leftHandSwitch;

        setSwitchState();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility();
            }
        });

        leftHandSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePreference();
            }
        });
    }

    public void changeVisibility() {
        if (settingsBar.getVisibility() == View.GONE) {
            settingsBar.setVisibility(View.VISIBLE);
            settingsBar.bringToFront();
            button.bringToFront();
        } else {
            settingsBar.setVisibility(View.GONE);
        }
    }

    private void setSwitchState() {
        String savedLefHandTribe = sharedPreferences.getString("left_hand", "off");
        if (savedLefHandTribe.equals("on")) {
            leftHandSwitch.setText("Włączony");
            leftHandSwitch.setChecked(true);
        } else {
            leftHandSwitch.setText("Wyłączony");
            leftHandSwitch.setChecked(false);
        }
    }

    public void changePreference(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentState = sharedPreferences.getString("left_hand", "off");

        if (currentState.equals("on")) {
            editor.putString("left_hand", "off");
            leftHandSwitch.setText("Wyłączony");
            leftHandSwitch.setChecked(false);
        } else {
            editor.putString("left_hand", "on");
            leftHandSwitch.setText("Włączony");
            leftHandSwitch.setChecked(true);
        }
        editor.apply();
    }
}
