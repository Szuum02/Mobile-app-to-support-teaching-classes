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
        String type = "teacher";

        //Todo zrobić połączenie z bazą danych jak będzie gotowa

        try{
            if(type == null){
                throw new NullPointerException("Cannot find your login data in our dataBase");
            } else if (type.equals("teacher")) {
                Intent intent = new Intent(this, ChooseGroup.class);
                startActivity(intent);
            } else if (type.equals("student")) {
                Intent intent = new Intent(this, TmpStudentDefaultView.class);
                startActivity(intent);
            }
        } catch (NullPointerException e){
            Toast errorToast = Toast.makeText(MainActivity.this,
                    "Cannot find your login data in our dataBase", Toast.LENGTH_SHORT);
            errorToast.show();
            Log.v("errorLog", "working");
        }
    }
}