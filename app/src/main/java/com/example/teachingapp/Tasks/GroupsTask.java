package com.example.teachingapp.Tasks;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teachingapp.DayOfWeekMapper;
import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.AllGroups;
import com.example.teachingapp.Teacher.TeacherMainPage;
import com.example.teachingapp.Teacher.PresenceOrActivity;
import com.example.teachingapp.dtos.LessonDTO;
import com.example.teachingapp.models.Group;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GroupsTask {
    private static final String TAG = "FetchGroupsTask";
    private static final String ERROR_MESSAGE = "Cannot fetch groups";

    private AllGroups activity;
    private SharedPreferences sharedPreferences;
    private String lastDate;
    private TextView textView;
    AlertDialog dialog;

    public GroupsTask(AllGroups activity, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
        LocalDate today = LocalDate.now();
        this.lastDate = today.minusDays(1).toString();
        textView = activity.findViewById(R.id.weekDay);
    }

    public void startTask() {
        Map<String, List<LessonDTO>> lessonsMap = getLessons();
        Set<String> dates = lessonsMap.keySet();

        lastDate = findClosestDate(dates);

        setButtons(dates, lessonsMap);

        showGroup(dates, lessonsMap);

    }

    private void showGroup(Set<String> dates, Map<String, List<LessonDTO>> lessonsMap) {
        if (dates != null && !dates.isEmpty() && lastDate != null) {
            addGroups(dates, lessonsMap);
        } else {
            Toast.makeText(activity, "Brak grup do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    public void addGroups(Set<String> dates, Map<String, List<LessonDTO>> lessonsMap) {
        LinearLayout layout = activity.findViewById(R.id.linearLayout);
        layout.removeAllViews();

        setTextView();

        for (LessonDTO lesson : lessonsMap.get(lastDate)) {
            Long groupId = lesson.getGroupId();
            String subject = lesson.getTopic();

            Button button = new Button(activity);
            button.setText(subject);
            button.setBackgroundResource(R.drawable.group_button);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(0, 0, 0, 16);
            button.setLayoutParams(params);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLessons(groupId);
                }
            });
            layout.addView(button);
        }
    }

    private void showLessons(Long groupId){
        Intent intent = new Intent(activity, PresenceOrActivity.class);
        intent.putExtra("group_id", groupId);
        activity.startActivity(intent);
    }

    public Map<String, List<LessonDTO>> getLessons() {
        String json = sharedPreferences.getString("lessons", null);
        if (json == null) {
            return new HashMap<>();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<LessonDTO>>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public String findClosestDate(Set<String> dates) {
        String upcoming = null;
        String past = null;

        for(String day : dates) {
            if (Period.between(LocalDate.parse(lastDate), LocalDate.parse(day)).getDays() > 0) {
                upcoming = findUpcoming(upcoming, day);
            } else {
                past = findPast(past, day);
            }
        }
        if (upcoming != null){return upcoming;}
        return past;
    }

    public String findClosestUpcomingDate(Set<String> dates) {
        String upcoming = null;

        for(String day : dates) {
            if (Period.between(LocalDate.parse(lastDate), LocalDate.parse(day)).getDays() > 0) {
                upcoming = findUpcoming(upcoming, day);
            }
        }
        return upcoming;
    }

    public String findClosestPastDate(Set<String> dates) {
        String past = null;

        for(String day : dates) {
            if (Period.between(LocalDate.parse(lastDate), LocalDate.parse(day)).getDays() < 0) {
                past = findPast(past, day);
            }
        }
        return past;
    }

    public String findUpcoming(String upcoming, String day){
        if(upcoming == null){
            return day;
        }
        if (Period.between(LocalDate.parse(lastDate), LocalDate.parse(day)).getDays() <
                Period.between(LocalDate.parse(lastDate), LocalDate.parse(upcoming)).getDays()) {
            return day;
        }
        return upcoming;
    }

    public String findPast(String past, String day) {
        if (past == null) {
            return day;
        }
        if (Period.between(LocalDate.parse(lastDate), LocalDate.parse(day)).getDays() >
                Period.between(LocalDate.parse(lastDate), LocalDate.parse(past)).getDays()) {
            return day;
        }
        return past;
    }

    public void setTextView() {
        String dayOfWeek = DayOfWeekMapper.mapEnglishToPolish(LocalDate.parse(lastDate).getDayOfWeek().toString());
        SpannableStringBuilder builder = new SpannableStringBuilder();

        int dayOfWeekStart = builder.length();
        builder.append(dayOfWeek);
        int dayOfWeekEnd = builder.length();

        int dateStart = builder.length();
        builder.append("\n" + lastDate);
        int dateEnd = builder.length();

        builder.setSpan(new StyleSpan(Typeface.NORMAL), dateStart, dateEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), dayOfWeekStart, dayOfWeekEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new RelativeSizeSpan(1f), dateStart, dateEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Mała czcionka dla daty
        builder.setSpan(new RelativeSizeSpan(2f), dayOfWeekStart, dayOfWeekEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // Duża czcionka dla dnia tygodnia

        textView.setText(builder);
        textView.setGravity(Gravity.CENTER);
    }

    public void setButtons(Set<String> dates, Map<String, List<LessonDTO>> lessonsMap) {
        String savedLefHandTribe = sharedPreferences.getString("left_hand", "off");

        Button nextButton = activity.findViewById(R.id.nextButton);
        Button prevButton = activity.findViewById(R.id.prevButton);
        Button leftDates = activity.findViewById(R.id.datyLeft);
        Button rightDates = activity.findViewById(R.id.datyRight);

        if(savedLefHandTribe.equals("on")){
            rightDates.setVisibility(View.GONE);
            rightDates.setEnabled(false);
        }
        else {
            leftDates.setVisibility(View.GONE);
            leftDates.setEnabled(false);
        }

        leftDates.setBackgroundResource(R.drawable.button_background);
        rightDates.setBackgroundResource(R.drawable.button_background);

        leftDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatesDialog(dates);
            }
        });

        rightDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatesDialog(dates);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String findDate = findClosestUpcomingDate(dates);
                if(findDate != null){
                    lastDate = findDate;
                    showGroup(dates, lessonsMap);
                }
                else {
                    Toast.makeText(activity, "Dotarłeś do ostatniej zarejestrowanej daty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextButton.setBackgroundResource(R.drawable.button_background);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String findDate = findClosestPastDate(dates);
                if(findDate != null){
                    lastDate = findDate;
                    showGroup(dates, lessonsMap);
                }
                else {
                    Toast.makeText(activity, "Dotarłeś do ostatniej zarejestrowanej daty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        prevButton.setBackgroundResource(R.drawable.button_background);
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
                    lastDate = date;
                    showGroup(dates, getLessons());
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
