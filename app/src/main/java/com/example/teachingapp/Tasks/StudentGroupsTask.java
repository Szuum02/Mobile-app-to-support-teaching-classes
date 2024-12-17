package com.example.teachingapp.Tasks;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.AddGroup;
import com.example.teachingapp.Student.ShowActivityGroupRanking;
import com.example.teachingapp.Student._StudentChooseGroup;
import com.example.teachingapp.dtos.LessonDTO;
import com.example.teachingapp.dtos.StudentDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

public class StudentGroupsTask {
    private _StudentChooseGroup activity;
    private SharedPreferences sharedPreferences;
    private Button addGroupButton;
    private Button returnButton;
    private String chosenDate;
    private StudentDTO studentDTO;
    private TextView chooseDateTextView;
    private LinearLayout linearLayout;
    private TextView showAllClassesTexView;
    private int chosenYear;
    private int chosenMonth;
    private int chosenDay;

    public StudentGroupsTask(_StudentChooseGroup activity, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
        this.addGroupButton = activity.findViewById(R.id.add_group_button);
        this.returnButton = activity.findViewById(R.id.return_button);
        this.studentDTO = getStudentDto();
        this.chooseDateTextView = activity.findViewById(R.id.choose_date);
        this.linearLayout = activity.findViewById(R.id.linearLayout);
        this.chosenDate = null;
        this.showAllClassesTexView = activity.findViewById(R.id.all_subjects);
        Calendar calendar = Calendar.getInstance();
        chosenYear = calendar.get(Calendar.YEAR);
        chosenMonth = calendar.get(Calendar.MONTH);
        chosenDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void startTask() {
        setUpChooseDateTextView();
        setUpAddGroupButton();
        setUpReturnButton();
        setUpShowAllClassesTexView();
        showClasses();
    }

    private void setUpAddGroupButton() {
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddGroup.class);
                intent.putExtra("student_id", studentDTO.getId());
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    private void setUpReturnButton() {
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
    }

    private void showClasses() {
        if(chosenDate == null) {
            showAllClasses();
        } else {
            showChosenDateClasses();
        }
    }

    private void showChosenDateClasses() {
        linearLayout.removeAllViews();
        List<LessonDTO> lessonList = studentDTO.getLessons().get(chosenDate);
        if(lessonList != null){
            int counter = 0;
            for (LessonDTO lesson :  lessonList) {
                LocalDateTime lessonDate  = lesson
                        .getDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                linearLayout.addView(createButton(lesson, counter, lesson));
                counter++;
            }
        }
        else {
            Toast.makeText(activity, "Nie ma zajęć dla wybranej daty", Toast.LENGTH_SHORT).show();
        }

    }

    private void showAllClasses() {
        int counter = 0;
        linearLayout.removeAllViews();
        for (List<LessonDTO> lessonList : studentDTO.getLessons().values()) {
            for (LessonDTO lesson : lessonList) {
                linearLayout.addView(createButton(lesson, counter, lesson));
                counter++;
            }
        }
    }

    private Button createButton(LessonDTO lesson, int counter, LessonDTO lessonDTO) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dpToPx(332),
                dpToPx(76)
        );
        params.setMargins(21, 22, 0, 22);

        Button button = new Button(activity);
        setButtonColor(button, counter%3);
        button.setLayoutParams(params);
        setButtonText(button, lesson);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ShowActivityGroupRanking.class);
                intent.putExtra("group_id", lessonDTO.getGroupId());
                intent.putExtra("student_id", studentDTO.getId());
                intent.putExtra("nick", studentDTO.getNick());
                activity.startActivity(intent);
            }
        });

        return button;
    }

    private void setButtonText(Button button, LessonDTO lesson) {
        String topic = lesson.getTopic();

        String date = getDateFromData(lesson);

        String classRoom = String.valueOf(lesson.getClassroom());

        String combinedText = topic + "\n" + date + ", " + classRoom;


        SpannableString spannableString = new SpannableString(combinedText);

        int topicEnd = topic.length();
        spannableString.setSpan(
                new RelativeSizeSpan(1.25f),
                0,
                topicEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );


        int dateEnd = topicEnd + date.length() + 1;
        spannableString.setSpan(
                new ForegroundColorSpan(Color.parseColor("#474747")),
                topicEnd + 2,
                dateEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannableString.setSpan(
                new ForegroundColorSpan(Color.parseColor("#474747")),
                dateEnd + 2,
                combinedText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        button.setGravity(Gravity.LEFT);
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setPadding(dpToPx(20), 0, 0, 0);
        button.setText(spannableString);
    }

    private String getDateFromData(LessonDTO upcomingLesson) {
        LocalDateTime localDateTime = upcomingLesson.getDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return addZeroToString(String.valueOf(localDateTime.getDayOfMonth()))
                + "-" + addZeroToString(String.valueOf(localDateTime.getMonth().getValue()))
                + "-" + localDateTime.getYear()
                + ", " + addZeroToString(String.valueOf(localDateTime.getHour()))
                + ":" + addZeroToString(String.valueOf(localDateTime.getMinute()));
    }

    private String addZeroToString(String string) {
        if(string.length() < 2){
            return "0" + string;
        }
        return string;
    }

    private void setUpChooseDateTextView() {
        chooseDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    private StudentDTO getStudentDto() {
        String json = sharedPreferences.getString("student_data", null);
        if (json == null) {
            return null;
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<StudentDTO>() {}.getType();
            return gson.fromJson(json, type);
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    chosenDate = selectedYear + "-"
                            + addZeroToString(String.valueOf(selectedMonth + 1))
                            + "-" + addZeroToString(String.valueOf(selectedDay));
                    chosenYear = selectedYear;
                    chosenMonth = selectedMonth;
                    chosenDay = selectedDay;
                    showClasses();
                },
                chosenYear, chosenMonth, chosenDay
        );

        datePickerDialog.show();
    }

    private int dpToPx(int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void setButtonColor(Button button, int counter){
        switch (counter) {
            case 0:
                button.setBackgroundResource(R.drawable._red_button_background);
                break;
            case 1:
                button.setBackgroundResource(R.drawable._green_button_background);
                break;
            case 2:
                button.setBackgroundResource(R.drawable._gray_light_button_background);
                break;
            default:
        }
    }

    public void setUpShowAllClassesTexView() {
        showAllClassesTexView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                chosenYear = calendar.get(Calendar.YEAR);
                chosenMonth = calendar.get(Calendar.MONTH);
                chosenDay = calendar.get(Calendar.DAY_OF_MONTH);
                chosenDate = null;
                showAllClasses();
            }
        });
    }
}
