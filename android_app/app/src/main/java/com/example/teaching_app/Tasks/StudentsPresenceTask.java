package com.example.teaching_app.Tasks;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teaching_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.teaching_app.Teacher.CheckPresence;
import com.example.teaching_app.Teacher.ChooseGroup;
import com.example.teaching_app.retrofit.LessonApi;
import com.example.teaching_app.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsPresenceTask {
    private final CheckPresence activity;
    private final long lessonId;
//    private HashMap<String, Student> studentHashMap;

    public StudentsPresenceTask(CheckPresence activity, long lessonId) {
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

            ArrayList<TextView> textViewList = new ArrayList<>();
            for (Object[] student : students) {
                long studentId = ((Double) student[0]).longValue();
                String name = (String) student[1];
                String lastName = (String) student[2];
                int index = ((Double) student[3]).intValue();

                // poziom przycisków
                LinearLayout layout = new LinearLayout(activity);
                layout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setPadding(0, 10, 0, 10);

                // Tworzenie miejsca na imie i nazwisko
                TextView textView = new TextView(activity);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                textView.setText(name + " " + lastName);
                layout.addView(textView);
                textViewList.add(textView);

                // Tworzenie przycisków
                Button presenceButton = new Button(activity);
                Button absenceButton = new Button(activity);
                Button lateButton = new Button(activity);
                addPresenceButton(layout, studentId, presenceButton, List.of(absenceButton, lateButton));
                addAbsenceButton(layout, studentId, absenceButton, List.of(presenceButton, lateButton));
                addLateButton(layout, studentId, lateButton, List.of(presenceButton, absenceButton));

                // Dodanie zestawu do głównego layoutu
                dynamicLayout.addView(layout);
            }
        }
        else {
            Toast.makeText(activity, "Brak studentów do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }


    public void addPresenceButton(LinearLayout layout, long studentId, Button button, List<Button> otherButtons) {
//        Button button = new Button(activity);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("O");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertPresence insertPresence = new InsertPresence(studentId, 1, lessonId, button, otherButtons);
                insertPresence.execute();
            }
        });
        layout.addView(button);
    }

    public void addAbsenceButton(LinearLayout layout, long studentId, Button button, List<Button> otherButtons){
//        Button button = new Button(activity);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("N");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertPresence insertPresence = new InsertPresence(studentId, 2, lessonId, button, otherButtons);
                insertPresence.execute();

            }
        });
        layout.addView(button);
    }

    public void addLateButton(LinearLayout layout, long studentId, Button button, List<Button> otherButtons){
//        Button button = new Button(activity);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("S");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertPresence insertPresence = new InsertPresence(studentId, 3, lessonId, button, otherButtons);
                insertPresence.execute();

            }
        });
        layout.addView(button);
    }
}
