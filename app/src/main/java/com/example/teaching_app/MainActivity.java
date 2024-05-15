package com.example.teaching_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teaching_app.Student.TmpStudentDefaultView;
import com.example.teaching_app.Teacher.ChooseGroup;

public class MainActivity extends AppCompatActivity {

    private EditText loginText;
    private  EditText passwordText;
    private Button loginButton;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        loginText = findViewById(R.id.LoginText);
        passwordText = findViewById(R.id.PasswordText);
        loginButton = findViewById(R.id.button);

    }


    public void checkLoginData(View view){
        String login = loginText.getText().toString();
        String password = passwordText.getText().toString();
        int type = 3;

        // Execute AsyncTask to fetch user data from database
        DatabaseTask databaseTask = new DatabaseTask(this, login, password);
        databaseTask.execute();
    }
}