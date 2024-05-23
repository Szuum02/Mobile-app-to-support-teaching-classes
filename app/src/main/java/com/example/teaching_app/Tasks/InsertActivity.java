package com.example.teaching_app.Tasks;
import android.os.AsyncTask;
import android.util.Log;

import com.example.teaching_app.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class InsertActivity extends AsyncTask<Void, Void, Void>{

    private final int studentId;
    private final String date;
    private final int points;

    public InsertActivity(int studentId, String date, int points) {
        this.studentId = studentId;
        this.date = date;
        this.points = points;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            if (connection != null) {
                String query = "INSERT INTO Activities (student_id, date, points) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setInt(1, studentId);
                statement.setString(2, date);
                statement.setInt(3, points);
                Log.d("SQLSTAT", statement.toString());
                statement.executeUpdate();
            } else {
                Log.d("polonczenie", "Brak połączenia z bazą danych");
            }
        } catch (SQLException e) {
            Log.e("SQLException", "Błąd podczas wykonywania zapytania SQL", e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Log.e("SQLException", "Błąd podczas zamykania połączenia", e);
            }
        }

        return null;
    }
}
