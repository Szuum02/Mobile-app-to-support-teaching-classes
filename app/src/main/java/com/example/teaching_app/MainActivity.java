package com.example.teaching_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.teaching_app.Tasks.LoginTask;

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

        // Execute AsyncTask to fetch user data from database
        LoginTask loginTask = new LoginTask(this, login, password);
        loginTask.execute();
    }
}