package com.example.teachingapp.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.CheckActivity;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.retrofit.Api.LessonApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.time.LocalDateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsActivityTask {


    private final CheckActivity activity;
    private final long lessonId;
    //    private HashMap<String, Student> studentHashMap;
    public StudentsActivityTask(CheckActivity activity, long lessonId) {
        this.activity = activity;
        this.lessonId = lessonId;
//        this.studentHashMap = new HashMap<>();
    }

    public void findAndShowStudents() {
        RetrofitService retrofitService = new RetrofitService();
        LessonApi lessonApi = retrofitService.getRetrofit().create(LessonApi.class);

        lessonApi.getStudents(lessonId)
                .enqueue(new Callback<List<Object[]>>() {
                    @Override
                    public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
                        showStudents(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Object[]>> call, Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ChooseGroup.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }

    private void showStudents(List<Object[]> students) {
        if (students != null && !students.isEmpty()) {
            LinearLayout dynamicLayout = activity.findViewById(R.id.dynamic_layout);
            dynamicLayout.removeAllViews();

            for (Object[] student : students) {
                long studentId = ((Double) student[0]).longValue();
                String name = (String) student[1];
                String lastName = (String) student[2];


                LinearLayout layout = new LinearLayout(activity);
                layout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setPadding(0, 10, 0, 10);

                TextView textView = new TextView(activity);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                textView.setText(name + " " + lastName);
                layout.addView(textView);

                addPointsButton(layout, "+1", studentId, name + " " + lastName, textView);
                addPointsButton(layout, "-1", studentId, name + " " + lastName, textView);

                dynamicLayout.addView(layout);
            }
        } else {
            Toast.makeText(activity, "Brak studentów do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }


    private void addPointsButton(LinearLayout layout, String buttonText, long studentId, String fullName, TextView textView) {
        Button button = new Button(activity);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText(buttonText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Student tmp = studentHashMap.get(student.getName() + " " + student.getLastname());
                int points = (buttonText.equals("+1")) ? 1: -1;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    InsertActivity task = new InsertActivity(studentId, lessonId, points, fullName, textView);
                    task.addActivity();
                }
            }
        });

        layout.addView(button);
    }



}
