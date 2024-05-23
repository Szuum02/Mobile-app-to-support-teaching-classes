package com.example.teaching_app.Tasks;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.example.teaching_app.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class InsertPresence extends AsyncTask<Void, Void, Void>{

    private final int studentId;
    private final int presenceTypeId;

    public InsertPresence(int studentId, int presenceTypeId) {
        this.studentId = studentId;
        this.presenceTypeId = presenceTypeId;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Connection connection = null;
        PreparedStatement deleteStatement = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            if (connection != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate currentDate = LocalDate.now();

                String deleteQuery = "DELETE FROM Presences WHERE student_id = ? AND Day = ? AND Month = ? AND Year = ?";
                deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, studentId);
                deleteStatement.setInt(2, currentDate.getDayOfMonth());
                deleteStatement.setInt(3, currentDate.getMonthValue());
                deleteStatement.setInt(4, currentDate.getYear());
                deleteStatement.executeUpdate();


                String query = "INSERT INTO Presences (presence_type_id, student_id, date, Day, Month, Year) VALUES (?, ?, ?, ?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setInt(1, presenceTypeId);
                statement.setInt(2, studentId);
                statement.setString(3, currentDate.toString()); // Dodanie aktualnej daty
                statement.setInt(4, currentDate.getDayOfMonth()); // Dodanie dnia
                statement.setInt(5, currentDate.getMonthValue()); // Dodanie miesiąca
                statement.setInt(6, currentDate.getYear()); // Dodanie roku

                Log.d("data", currentDate.toString());
                Log.d("data", String.valueOf(currentDate.getDayOfMonth()));
                Log.d("data", String.valueOf(currentDate.getMonthValue()));
                Log.d("data", String.valueOf(currentDate.getYear()));

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
