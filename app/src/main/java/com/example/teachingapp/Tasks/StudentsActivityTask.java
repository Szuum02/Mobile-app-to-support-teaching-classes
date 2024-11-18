package com.example.teachingapp.Tasks;

import android.content.Intent;
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

import androidx.annotation.NonNull;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.ActivityHistory;
import com.example.teachingapp.Teacher.CheckActivity;
import com.example.teachingapp.Teacher.CheckPresence;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.retrofit.Api.LessonApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsActivityTask {
    private static int VERTICAL = 1;
    private static int HORIZONTAL = 0;
    private final CheckActivity activity;
    private final long groupId;
    private SharedPreferences sharedPreferences;
    String leftHandTribe;
    private int counter;
    Map<Integer, Boolean> isToColorMap = new HashMap<>();

    public StudentsActivityTask(CheckActivity activity, long groupId, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.groupId = groupId;
        this.sharedPreferences = sharedPreferences;
        setLayoutButtons();
        isToColorMap.put(0, false);
        isToColorMap.put(1, false);
        isToColorMap.put(2, true);
        isToColorMap.put(3, true);
    }

    public void findAndShowStudents() {
        RetrofitService retrofitService = new RetrofitService();
        LessonApi lessonApi = retrofitService.getRetrofit().create(LessonApi.class);
        counter = 0;

        lessonApi.getStudents(groupId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Object[]>> call,
                                           @NonNull Response<List<Object[]>> response) {
                        leftHandTribe = sharedPreferences.getString("left_hand", "off");
                        showStudents(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Object[]>> call,
                                          @NonNull Throwable t) {
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

                TextView nameView = prepareNameTextView(name + " " + lastName);
                TextView pointsView = preparePointsTextView( " ");

                LinearLayout layout = prepareMainLinearLayout();

                if(leftHandTribe.equals("on")) {
                    layout.addView(setUpLayoutWithButtons(nameView, pointsView, studentId, name, lastName));
                    layout.addView(setUpLayoutWithTexView(nameView, pointsView));}
                else {
                    layout.addView(setUpLayoutWithTexView(nameView, pointsView));
                    layout.addView(setUpLayoutWithButtons(nameView, pointsView, studentId, name, lastName));}

                if(isToColorMap.get(counter%4)) {
                    layout.setBackgroundResource(R.drawable.basic_texview);}
                else {
                    layout.setBackgroundResource(R.drawable.brown_textview);}

                counter++;
                dynamicLayout.addView(layout);
            }
        } else {
            Toast.makeText(activity, "Brak studentów do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }


    public TextView prepareNameTextView(String name) {
        TextView textView = new TextView(activity);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f));
        textView.setText(String.format("%s ", name));
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(16, 8, 16, 8);
        textView.setSingleLine(false);
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        return textView;
    }

    public TextView preparePointsTextView(String name) {
        TextView textView = new TextView(activity);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        textView.setText(String.format("%s ", name));
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(16, 8, 16, 8);
        textView.setSingleLine(false);
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);

        return textView;
    }

    public LinearLayout setUpLayoutWithTexView(TextView nameView, TextView pointsView) {
        LinearLayout layout = prepareSideLinearLayout(HORIZONTAL, 5);

        if(leftHandTribe.equals("on")){
            layout.addView(pointsView);
            layout.addView(nameView);
        } else {
            layout.addView(nameView);
            layout.addView(pointsView);
        }

        return layout;
    }

    public LinearLayout setUpLayoutWithButtons(TextView nameView, TextView pointsView, Long studentId, String name, String lastName) {
        LinearLayout layout = prepareSideLinearLayout(HORIZONTAL,4);

        Button pluseButton = new Button(activity);
        Button minusButton = new Button(activity);
        Button informationButton = new Button(activity);

        addPlusButton(pluseButton, layout, "+", studentId, name + " " + lastName, nameView, pointsView) ;
        addInformationButton(minusButton, layout, "?", studentId, name + " " + lastName, nameView, pointsView);
        addMinusButton(informationButton, layout, "-", studentId, name + " " + lastName, nameView, pointsView);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        pluseButton.setLayoutParams(buttonParams);
        informationButton.setLayoutParams(buttonParams);
        minusButton.setLayoutParams(buttonParams);

        return layout;
    }

    public LinearLayout prepareSideLinearLayout(Integer orientation, float weight) {
        LinearLayout layout = new LinearLayout(activity);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, weight));

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


    private void addPlusButton(Button button, LinearLayout layout, String buttonText,
                                 long studentId, String fullName, TextView nameView, TextView pointsView) {
        button.setText(buttonText);
        button.setBackgroundResource(R.drawable.green_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int points = 1;
                InsertActivity task = new InsertActivity(studentId, groupId, points, fullName, nameView, pointsView);
                task.addActivity();
            }
        });

        layout.addView(button);
    }

    private void addMinusButton(Button button, LinearLayout layout, String buttonText,
                                 long studentId, String fullName, TextView nameView, TextView pointsView) {
        button.setText(buttonText);
        button.setBackgroundResource(R.drawable.red_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int points = -1;
                InsertActivity task = new InsertActivity(studentId, groupId, points, fullName, nameView, pointsView);
                task.addActivity();
            }
        });

        layout.addView(button);
    }

    private void addInformationButton(Button button, LinearLayout layout, String buttonText,
                                long studentId, String fullName, TextView nameView, TextView pointsView) {
        button.setText(buttonText);
        button.setBackgroundResource(R.drawable.blue_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ActivityHistory.class);
                intent.putExtra("studentId", studentId);
                intent.putExtra("group_id", groupId);
                activity.startActivity(intent);
            }
        });

        layout.addView(button);
    }

    public void setLayoutButtons() {
        Button presenceButton = activity.findViewById(R.id.presence_button);
        Button refresh = activity.findViewById(R.id.refresh_button);
        Button timeTableButton = activity.findViewById(R.id.sheet_button);

        presenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CheckPresence.class);
                intent.putExtra("group_id", groupId);
                activity.startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findAndShowStudents();
            }
        });

        timeTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ChooseGroup.class);
                activity.startActivity(intent);
            }
        });
    }
}
