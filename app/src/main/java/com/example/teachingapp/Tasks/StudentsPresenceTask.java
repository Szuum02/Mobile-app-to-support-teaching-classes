package com.example.teachingapp.Tasks;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.CheckActivity;
import com.example.teachingapp.Teacher.CheckPresence;
import com.example.teachingapp.Teacher.TeacherMainPage;
import com.example.teachingapp.Teacher.PresenceHistory;
import com.example.teachingapp.dtos.LessonDTO;
import com.example.teachingapp.retrofit.Api.LessonApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsPresenceTask {
    private final CheckPresence activity;
    private final long groupId;
    private static int VERTICAL = 1;
    private static int HORIZONTAL = 0;
    private SharedPreferences sharedPreferences;
    String leftHandTribe;
    private int counter;
    Map<Integer, Boolean> isToColorMap = new HashMap<>();
    AlertDialog dialog;


    public StudentsPresenceTask(CheckPresence activity, long groupId, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.groupId = groupId;
        this.sharedPreferences = sharedPreferences;
        isToColorMap.put(0, false);
        isToColorMap.put(1, false);
        isToColorMap.put(2, true);
        isToColorMap.put(3, true);

        setLayoutButtons();
    }

    public void findAndShowStudents() {
        counter = 0;
        RetrofitService retrofitService = new RetrofitService();
        LessonApi lessonApi = retrofitService.getRetrofit().create(LessonApi.class);

        lessonApi.getStudents(groupId)
                .enqueue(new Callback<List<Object[]>>() {
                    @Override
                    public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
                        leftHandTribe = sharedPreferences.getString("left_hand", "off");
                        showStudents(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Object[]>> call, Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(TeacherMainPage.class.getName()).log(Level.SEVERE, "Error occurred", t);
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
                Button studentButton = prepareStudentButton(name, lastName);

                studentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, PresenceHistory.class);
                        intent.putExtra("studentId", studentId);
                        activity.startActivity(intent);
                    }
                });


                LinearLayout layout = prepareMainLinearLayout();

                if(leftHandTribe.equals("on")) {
                    layout.addView(setUpLayoutWithButtons(studentButton, studentId));
                    layout.addView(setUpLayoutWithStudentButton(studentButton));
                }
                else {
                    layout.addView(setUpLayoutWithStudentButton(studentButton));
                    layout.addView(setUpLayoutWithButtons(studentButton, studentId));
                }
                dynamicLayout.addView(layout);
            }
        } else {
            Toast.makeText(activity, "Brak studentów do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }


    public Button prepareStudentButton(String name, String lastName) {
        Button studentButton = new Button(activity);
        studentButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        studentButton.setText(name + " " + lastName);
        studentButton.setTextSize(15);
        studentButton.setTextColor(Color.BLACK);
        studentButton.setPadding(16, 8, 16, 8);
        studentButton.setSingleLine(false);
        studentButton.setMaxLines(2);
        studentButton.setEllipsize(TextUtils.TruncateAt.END);
        studentButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

        if(isToColorMap.get(counter%4)){
            studentButton.setBackgroundResource(R.drawable.basic_texview);
        } else {
            studentButton.setBackgroundResource(R.drawable.brown_textview);
        }

        counter++;

        return studentButton;
    }

    public LinearLayout setUpLayoutWithStudentButton(Button studentButton) {
        LinearLayout layout = prepareSideLinearLayout(VERTICAL);
        layout.addView(studentButton);

        return layout;
    }

    public LinearLayout setUpLayoutWithButtons(Button studentButton, Long studentId) {
        LinearLayout layout = prepareSideLinearLayout(HORIZONTAL);


        Button presenceButton = new Button(activity);
        Button absenceButton = new Button(activity);
        Button lateButton = new Button(activity);

        addPresenceButton(layout, studentId, presenceButton, List.of(absenceButton, lateButton), studentButton);
        addAbsenceButton(layout, studentId, absenceButton, List.of(presenceButton, lateButton), studentButton);
        addLateButton(layout, studentId, lateButton, List.of(presenceButton, absenceButton), studentButton);

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

    public void addPresenceButton(LinearLayout layout, long studentId, Button button, List<Button> unpressedButtons, Button studentButton) {
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("O");
        button.setBackgroundResource(R.drawable.green_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertPresence insertPresence = new InsertPresence(studentId, 1, groupId, button, unpressedButtons, studentButton, leftHandTribe);
                insertPresence.removeAndAddPresence();
            }
        });
        layout.addView(button);
    }

    public void addAbsenceButton(LinearLayout layout, long studentId, Button button, List<Button> unpressedButtons, Button studentButton){
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("N");
        button.setBackgroundResource(R.drawable.red_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertPresence insertPresence = new InsertPresence(studentId, 2, groupId, button, unpressedButtons, studentButton, leftHandTribe);
                insertPresence.removeAndAddPresence();
            }
        });
        layout.addView(button);
    }

    public void addLateButton(LinearLayout layout, long studentId, Button button, List<Button> unpressedButtons, Button studentButton){
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setText("S");
        button.setBackgroundResource(R.drawable.yellow_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertPresence insertPresence = new InsertPresence(studentId, 3, groupId, button, unpressedButtons, studentButton, leftHandTribe);
                insertPresence.removeAndAddPresence();
            }
        });
        layout.addView(button);
    }

    public void setLayoutButtons() {
        Button activityButton = activity.findViewById(R.id.acitivty_button);
        Button refresh = activity.findViewById(R.id.refresh_button);
        Button timeTableButton = activity.findViewById(R.id.timetable_button);
        Button nfcButton = activity.findViewById(R.id.nfc_button);
        Button datesButton = activity.findViewById(R.id.data_button);

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CheckActivity.class);
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
                Intent intent = new Intent(activity, TeacherMainPage.class);
                activity.startActivity(intent);
            }
        });

        nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO nfc dodac
            }
        });

        datesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatesDialog(getDates());
            }
        });
    }

    public Set<String> getDates() {
        String json = sharedPreferences.getString("lessons", null);
        if (json == null) {
            return new HashSet<>();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<LessonDTO>>>() {}.getType();
        Map<String, List<LessonDTO>> map = gson.fromJson(json, type);
        return map.keySet();
    }

    public void showDatesDialog(Set<String> dates) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        ScrollView scrollView = new ScrollView(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        for (String date : dates) {
            Button dateButton = new Button(activity);
            dateButton.setText(date);
            dateButton.setBackgroundResource(R.drawable.button_background);

            dateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            layout.addView(dateButton);
        }

        scrollView.addView(layout);
        builder.setView(scrollView);
        dialog = builder.create();
        dialog.show();
    }
}