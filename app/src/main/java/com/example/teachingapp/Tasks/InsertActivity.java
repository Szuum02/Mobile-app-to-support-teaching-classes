package com.example.teachingapp.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.teachingapp.models.Activity;
import com.example.teachingapp.retrofit.Api.ActivityApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertActivity extends AsyncTask<Void, Void, Void>{

    private final long studentId;
    private final long lessonId;
    private final int points;
    private final String fullName;
    private final TextView textView;

    public InsertActivity(long studentId, long lessonId, int points, String fullName, TextView textView) {
        this.studentId = studentId;
        this.lessonId = lessonId;
        this.points = points;
        this.fullName = fullName;
        this.textView = textView;
    }

    // todo -> zmienić na bardziej aktualny sposób
    @Override
    protected Void doInBackground(Void... voids) {
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);

        LocalDateTime currentDate = LocalDateTime.now();

        activityApi.addActivity(lessonId, studentId, currentDate, points)
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        updateText(response.body());
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.e("SQLException", "Server error while adding activity");
                    }
                });


//        Connection connection = null;
//        PreparedStatement statement = null;
//
//        try {
//            connection = DatabaseConnection.getConnection();
//            if (connection != null) {
//                String query = "INSERT INTO Activities (student_id, date, points) VALUES (?, ?, ?)";
//                statement = connection.prepareStatement(query);
//                statement.setInt(1, studentId);
//                statement.setString(2, date);
//                statement.setInt(3, points);
//                Log.d("SQLSTAT", statement.toString());
//                statement.executeUpdate();
//            } else {
//                Log.d("polonczenie", "Brak połączenia z bazą danych");
//            }
//        } catch (SQLException e) {
//            Log.e("SQLException", "Błąd podczas wykonywania zapytania SQL", e);
//        } finally {
//            try {
//                if (statement != null) statement.close();
//                if (connection != null) connection.close();
//            } catch (SQLException e) {
//                Log.e("SQLException", "Błąd podczas zamykania połączenia", e);
//            }
//        }
//
        return null;
    }

    private void updateText(int points) {
        String pointsText = (points < 0) ? String.valueOf(points) : String.format("+%d", points);
        textView.setText(fullName + " " + pointsText);
    }
}
