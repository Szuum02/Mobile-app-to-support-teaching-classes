package com.example.teaching_app;
import android.os.AsyncTask;
import android.widget.Toast;
import android.util.Log;
import android.content.Intent;
import android.widget.EditText;

import com.example.teaching_app.DatabaseClasses.User;
import com.example.teaching_app.Student.TmpStudentDefaultView;
import com.example.teaching_app.Teacher.ChooseGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTask extends AsyncTask<String, Void, User> {

    private static final String TAG = "DatabaseTask";
    private static final String ERROR_MESSAGE = "Cannot connect to database";

    private MainActivity activity;
    private String email;
    private String password;

    public DatabaseTask(MainActivity activity, String email, String password) {
        this.activity = activity;
        this.email = email;
        this.password = password;
    }

    @Override
    protected User doInBackground(String... strings) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            // Establishing database connection
            connection = DatabaseConnection.getConnection();
            if (connection != null) {
                // Query to fetch user data based on email and password
                String query = "SELECT * FROM Users WHERE mail = ? AND password = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, password);

                resultSet = statement.executeQuery();

                // If user found, create User object
                if (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    String userEmail = resultSet.getString("mail");
                    String userPassword = resultSet.getString("password");
                    int isStudent = resultSet.getInt("is_student");
                    user = new User(userId, userEmail, userPassword, isStudent);
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, "SQLException: " + e.getMessage());
        } finally {
            // Closing database resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Log.e(TAG, "SQLException: " + e.getMessage());
            }
        }

        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        if (user != null) {
            // User found, redirect to appropriate activity based on user type
            if (user.getIs_student() == 0) {
                Intent intent = new Intent(activity, ChooseGroup.class);
                activity.startActivity(intent);
            } else if (user.getIs_student() == 1) {
                Intent intent = new Intent(activity, TmpStudentDefaultView.class);
                activity.startActivity(intent);
            }
        } else {
            // User not found, display error message
            Toast.makeText(activity, ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            Log.v("errorLog", "working");
        }
    }
}

