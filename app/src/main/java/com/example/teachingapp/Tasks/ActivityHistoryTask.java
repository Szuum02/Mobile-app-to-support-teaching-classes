package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.ActivityHistory;
import com.example.teachingapp.Teacher.ChooseAction;
import com.example.teachingapp.Teacher.GenerateReport;
import com.example.teachingapp.Teacher.PresenceHistory;
import com.example.teachingapp.dtos.ActivityDTO;
import com.example.teachingapp.dtos.StudentHistoryDTO;
import com.example.teachingapp.retrofit.Api.ActivityApi;
import com.example.teachingapp.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHistoryTask {
    private final ActivityHistory activity;
    private final Long groupId;
    private final Long studentId;
    private LinearLayout linearLayout;
    private ImageButton presenceButton;
    private ImageButton reportButton;
    private ImageButton returnButton;
    private TextView descriptionTexView;

    public ActivityHistoryTask(ActivityHistory activity, Long studentId, Long groupId) {
        this.activity = activity;
        this.groupId = groupId;
        this.studentId = studentId;
        this.linearLayout = activity.findViewById(R.id.linearLayout);
        this.presenceButton = activity.findViewById(R.id.calendar_button);
        this.reportButton = activity.findViewById(R.id.report_button);
        this.returnButton = activity.findViewById(R.id.return_button);
        this.descriptionTexView = activity.findViewById(R.id.description_texView);
    }

    public void startTask() {
        getStudentActivity();
        setUpButtons();
    }

    private void getStudentActivity() {
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);
        activityApi.getStudentHistory(studentId, groupId)
                .enqueue(new Callback<StudentHistoryDTO>() {
                    @Override
                    public void onResponse(Call<StudentHistoryDTO> call, Response<StudentHistoryDTO> response) {
                        if(response.body() != null) {
                            setUpLayout(response.body());
                        } else {
                            Toast.makeText(activity, "Nie udało się pobrać historii", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<StudentHistoryDTO> call, Throwable t) {
                        Toast.makeText(activity, "Nie udało się pobrać historii", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void setUpLayout(StudentHistoryDTO studentHistoryDTO) {
        int counter = 0;
        if(studentHistoryDTO.getActivities().isEmpty()){
            Toast.makeText(activity, "Historia obecności jest pusta", Toast.LENGTH_SHORT).show();
        }
        for(ActivityDTO activityDTO: studentHistoryDTO.getActivities()) {
            LinearLayout rowLayout = new LinearLayout(activity);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dpToPx(52)
            ));
            rowLayout.setGravity(Gravity.CENTER_VERTICAL);
            if(counter% 2 == 0) {
                rowLayout.setBackgroundColor(Color.parseColor("#D5D4D4"));
            }
            counter++;
            rowLayout.addView(generateDateTexView(activityDTO));
            rowLayout.addView(generateTimeTexView(activityDTO));
            rowLayout.addView(generatePointsTexView(activityDTO));

            linearLayout.addView(rowLayout);

            descriptionTexView.setText(
                    new StringBuilder()
                            .append(studentHistoryDTO.getName())
                            .append(" ")
                            .append(studentHistoryDTO.getLastname())
                            .append(" ")
                            .append(studentHistoryDTO.getIndex()).toString()
            );
        }
    }

    private int dpToPx(int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public TextView generateDateTexView(ActivityDTO activityDTO) {
        TextView textView = new TextView(activity);
        textView.setText(new StringBuilder()
                .append(activityDTO.getDate().substring(0,10)));
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        ));
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins));
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    public TextView generateTimeTexView(ActivityDTO activityDTO) {
        TextView textView = new TextView(activity);
        textView.setText(new StringBuilder()
                .append(activityDTO.getDate().substring(11)));
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        ));
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins));
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    public TextView generatePointsTexView(ActivityDTO activityDTO) {
        TextView textView = new TextView(activity);
        textView.setText(new StringBuilder()
                .append(activityDTO.getPoints()));
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        ));
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins));
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    public void setUpButtons() {
        presenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PresenceHistory.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("student_id", studentId);
                activity.startActivity(intent);
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, GenerateReport.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("student_id", studentId);
                activity.startActivity(intent);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ChooseAction.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
            }
        });
    }
}
