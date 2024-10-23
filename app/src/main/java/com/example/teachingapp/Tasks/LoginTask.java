package com.example.teachingapp.Tasks;

import android.os.AsyncTask;
import android.widget.Toast;
import android.util.Log;
import android.content.Intent;

import com.example.teachingapp.MainActivity;
import com.example.teachingapp.Student.TmpStudentDefaultView;
import com.example.teachingapp.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LoginTask  {

    private static final String TAG = "LoginTask";
    private static final String ERROR_MESSAGE = "Cannot connect to database - wrong data";

    private MainActivity activity;
    private String email;
    private String password;

    public LoginTask(MainActivity activity, String email, String password) {
        this.activity = activity;
        this.email = email;
        this.password = password;
    }

}