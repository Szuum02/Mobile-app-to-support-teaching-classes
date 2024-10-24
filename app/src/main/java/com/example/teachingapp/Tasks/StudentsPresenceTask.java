package com.example.teachingapp.Tasks;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.CheckPresence;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.retrofit.Api.LessonApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsPresenceTask {
    private final CheckPresence activity;
    private final long lessonId;
    private static int VERTICAL = 1;
    private static int HORIZONTAL = 0;
    private SharedPreferences sharedPreferences;
    String leftHandTribe;

    public StudentsPresenceTask(CheckPresence activity, long lessonId, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.lessonId = lessonId;
        this.sharedPreferences = sharedPreferences;
    }

    public void findAndShowStudents() {
        RetrofitService retrofitService = new RetrofitService();
        LessonApi lessonApi = retrofitService.getRetrofit().create(LessonApi.class);

        lessonApi.getStudents(lessonId)
                .enqueue(new Callback<List<Object[]>>() {
                    @Override
                    public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
                        leftHandTribe = sharedPreferences.getString("left_hand", "off");
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

            for (Object[] student : students) {
                long studentId = ((Double) student[0]).longValue();
                String name = (String) student[1];
                String lastName = (String) student[2];
                TextView textView = prepareTextView(name, lastName);

                LinearLayout layout = prepareMainLinearLayout();

                if(leftHandTribe.equals("on")) {
                    layout.addView(setUpLayoutWithButtons(textView, studentId));
                    layout.addView(setUpLayoutWithTexView(textView));
                    dynamicLayout.addView(layout);
                }
                else {
                    layout.addView(setUpLayoutWithTexView(textView));
                    layout.addView(setUpLayoutWithButtons(textView, studentId));
                    dynamicLayout.addView(layout);
                }
            }
        } else {
            Toast.makeText(activity, "Brak studentów do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }


    public TextView prepareTextView(String name, String lastName) {
        TextView textView = new TextView(activity);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText(name + " " + lastName);
        textView.setBackgroundResource(R.drawable.basic_texview);
        textView.setTextSize(17);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(16, 8, 16, 8);
        textView.setSingleLine(false);
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

        return textView;
    }

    public LinearLayout setUpLayoutWithTexView(TextView textView) {
        LinearLayout layout = prepareSideLinearLayout(VERTICAL);
        layout.addView(textView);

        return layout;
    }

    public LinearLayout setUpLayoutWithButtons(TextView textView, Long studentId) {
        LinearLayout layout = prepareSideLinearLayout(HORIZONTAL);


        Button presenceButton = new Button(activity);
        Button absenceButton = new Button(activity);
        Button lateButton = new Button(activity);

        addPresenceButton(layout, studentId, presenceButton, List.of(absenceButton, lateButton), textView);
        addAbsenceButton(layout, studentId, absenceButton, List.of(presenceButton, lateButton), textView);
        addLateButton(layout, studentId, lateButton, List.of(presenceButton, absenceButton), textView);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        presenceButton.setLayoutParams(buttonParams);
        absenceButton.setLayoutParams(buttonParams);
        lateButton.setLayoutParams(buttonParams);

        return layout;
    }

    public LinearLayout prepareSideLinearLayout(Integer orientation) {
        LinearLayout layout = new LinearLayout(activity);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

        if (orientation == LinearLayout.VERTICAL) {
            layout.setOrientation(LinearLayout.VERTICAL);
        } else if (orientation == LinearLayout.HORIZONTAL) {
            layout.setOrientation(LinearLayout.HORIZONTAL);
        }

        return layout;
    }


    public LinearLayout prepareMainLinearLayout() {
        LinearLayout layout = new LinearLayout(activity);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(0, 10, 0, 10);

        return layout;
    }

    public void addPresenceButton(LinearLayout layout, long studentId, Button button, List<Button> unpressedButtons, TextView textView) {
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("O");
        button.setBackgroundResource(R.drawable.o_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertPresence insertPresence = new InsertPresence(studentId, 1, lessonId, button, unpressedButtons, textView, leftHandTribe);
                insertPresence.removeAndAddPresence();
            }
        });
        layout.addView(button);
    }

    public void addAbsenceButton(LinearLayout layout, long studentId, Button button, List<Button> unpressedButtons, TextView textView){
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("N");
        button.setBackgroundResource(R.drawable.n_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertPresence insertPresence = new InsertPresence(studentId, 2, lessonId, button, unpressedButtons, textView, leftHandTribe);
                insertPresence.removeAndAddPresence();
            }
        });
        layout.addView(button);
    }

    public void addLateButton(LinearLayout layout, long studentId, Button button, List<Button> unpressedButtons, TextView textView){
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("S");
        button.setBackgroundResource(R.drawable.s_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertPresence insertPresence = new InsertPresence(studentId, 3, lessonId, button, unpressedButtons, textView, leftHandTribe);
                insertPresence.removeAndAddPresence();
            }
        });
        layout.addView(button);
    }
}