package com.example.teaching_app.Tasks;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teaching_app.DatabaseClasses.StudentsWithPoints;
import com.example.teaching_app.DatabaseConnection;
import com.example.teaching_app.R;
import java.time.LocalDateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.teaching_app.DatabaseClasses.Student;
import com.example.teaching_app.Teacher.CheckActivity;



public class StudentsActivityTask extends AsyncTask<Integer, Void, ArrayList<StudentsWithPoints>> {


    private final static String query = "SELECT gr.student_id, st.name, st.lastname, st.index_number, " +
            "SUM(ac.points) AS points FROM StudentsGroups AS gr Left JOIN Students AS st " +
            "ON gr.student_id = st.student_id Left JOIN Activities AS ac ON st.student_id = " +
            "ac.student_id WHERE gr.group_id = ? GROUP BY gr.student_id, " +
            "st.name, st.lastname, st.index_number";

    private final CheckActivity activity;
    private final int id;
    private HashMap<String, StudentsWithPoints> studentHashMap;
    public StudentsActivityTask(CheckActivity activity, int id) {
        this.activity = activity;
        this.id = id;
        this.studentHashMap = new HashMap<>();
    }
    @Override
    protected ArrayList<StudentsWithPoints> doInBackground(Integer... groupIds) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<StudentsWithPoints> students = new ArrayList<>();
        Log.d("czek1", "czek1");
        try {
            connection = DatabaseConnection.getConnection();
            Log.d("czek2", "czek2");

            if (connection != null) {

                statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                resultSet = statement.executeQuery();
                Log.d("czek3", "czek2");

                while (resultSet.next()) {
                    int id = resultSet.getInt("student_id");
                    String name = resultSet.getString("name");
                    String lastname = resultSet.getString("lastname");
                    int index = resultSet.getInt("index_number");
                    int points = resultSet.getInt("points");
                    StudentsWithPoints student = new StudentsWithPoints(id,name, lastname, index, points);
                    students.add(student);
                    studentHashMap.put(student.getName() + " " + student.getLastname(),student);
                }
            }
        } catch (SQLException e) {
            Toast errorToast = Toast.makeText(activity,
                    "coś nie pykło ", Toast.LENGTH_SHORT);                               // dla ułatwienia, usunąć przed pokazaniem
            errorToast.show();
        } finally {
            // Closing database resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Toast errorToast = Toast.makeText(activity,
                        "coś nie pykło 2", Toast.LENGTH_SHORT);                               // dla ułatwienia, usunąć przed pokazaniem
            }
        }

        return students;
    }

    @Override
    protected void onPostExecute(ArrayList<StudentsWithPoints> students) {
        super.onPostExecute(students);

        if (students != null && !students.isEmpty()) {
            LinearLayout dynamicLayout = activity.findViewById(R.id.dynamic_layout);
            dynamicLayout.removeAllViews();

            for (StudentsWithPoints student : students) {
                LinearLayout layout = new LinearLayout(activity);
                layout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setPadding(0, 10, 0, 10);

                TextView nameTextView = new TextView(activity);
                nameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                nameTextView.setText(student.getName() + " " + student.getLastname());
                layout.addView(nameTextView);

                TextView pointsTextView = new TextView(activity);
                pointsTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                pointsTextView.setText(String.valueOf(student.getPoints()));
                layout.addView(pointsTextView);

                addPointsButton(layout, "+1", student, pointsTextView);
                addPointsButton(layout, "-1", student, pointsTextView);

                dynamicLayout.addView(layout);
            }
        } else {
            Toast.makeText(activity, "Brak studentów do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private void addPointsButton(LinearLayout layout, String buttonText, StudentsWithPoints student, TextView pointsTextView) {
        Button button = new Button(activity);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText(buttonText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentsWithPoints tmp = studentHashMap.get(student.getName() + " " + student.getLastname());
                int points = buttonText.equals("+1") ? 1 : -1;
                tmp.setPoints(tmp.getPoints() + points);
                pointsTextView.setText(String.valueOf(tmp.getPoints()));

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = currentDateTime.format(formatter);
                    Log.d("dataT", String.valueOf(tmp.getId()));
                    Log.d("dataT", formattedDateTime);
                    Log.d("dataT", String.valueOf(points));

                    InsertActivity task = new InsertActivity(tmp.getId(), formattedDateTime, points);
                    task.execute();
                }
            }
        });

        layout.addView(button);
    }




}
