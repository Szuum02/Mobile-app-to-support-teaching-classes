package com.example.teaching_app.Tasks;
import android.os.AsyncTask;
import android.widget.Toast;
import android.util.Log;
import android.content.Intent;

import com.example.teaching_app.DatabaseClasses.User;
import com.example.teaching_app.DatabaseConnection;
import com.example.teaching_app.MainActivity;
import com.example.teaching_app.Student.TmpStudentDefaultView;
import com.example.teaching_app.Teacher.ChooseGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginTask extends AsyncTask<String, Void, User> {

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

    @Override
    protected User doInBackground(String... strings) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = DatabaseConnection.getConnection();
            if (connection != null) {
                String query = "SELECT * FROM Users WHERE mail = ? AND password = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, password);

                resultSet = statement.executeQuery();

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
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                Log.e(TAG, "SQLException: " + e.getMessage());
            }
        }

        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        if (user != null) {
            if (user.getIs_student() == 0) {
                Intent intent = new Intent(activity, ChooseGroup.class);
                intent.putExtra("teacher_id", user.getUser_id());
                activity.startActivity(intent);
            } else if (user.getIs_student() == 1) {
                Intent intent = new Intent(activity, TmpStudentDefaultView.class);
                activity.startActivity(intent);
            }
        } else {
            Toast.makeText(activity, ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            Log.v("errorLog", "working");
        }
    }
}

