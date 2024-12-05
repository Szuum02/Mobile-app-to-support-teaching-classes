package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.teachingapp.Teacher.CheckActivity;
import com.example.teachingapp.Teacher.CheckPresence;
import com.example.teachingapp.Teacher.ChooseAction;
import com.example.teachingapp.Teacher.GenerateReport;
import com.example.teachingapp.Teacher.PresenceHistory;
import com.example.teachingapp.Teacher.ShowQR;
import com.example.teachingapp.Teacher.TeacherScanQR;
import com.example.teachingapp.dtos.LessonDTO;
import com.example.teachingapp.dtos.PresenceDTO;
import com.example.teachingapp.dtos.StudentDataDTO;
import com.example.teachingapp.dtos.StudentPresenceHistoryDTO;
import com.example.teachingapp.enums.PresenceType;
import com.example.teachingapp.retrofit.Api.LessonApi;
import com.example.teachingapp.retrofit.Api.PresenceApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresenceHistoryTeacherTask {

    private final PresenceHistory activity;
    private final Long groupId;
    private final Long studentId;
    private LinearLayout linearLayout;
    private ImageButton activityButton;
    private ImageButton reportButton;
    private ImageButton returnButton;
    private TextView descriptionTexView;


    public PresenceHistoryTeacherTask(PresenceHistory activity, Long groupId, Long studentId) {
        this.activity = activity;
        this.groupId = groupId;
        this.studentId = studentId;
        this.linearLayout = activity.findViewById(R.id.linearLayout);
        this.activityButton = activity.findViewById(R.id.plus_minus_button);
        this.reportButton = activity.findViewById(R.id.report_button);
        this.returnButton = activity.findViewById(R.id.return_button);
        this.descriptionTexView = activity.findViewById(R.id.description_texView);
    }

    public void startTask() {
        getStudentActivityHistory();
        setUpButtons();
    }

    private void getStudentActivityHistory() {
        RetrofitService retrofitService = new RetrofitService();
        PresenceApi presenceApi = retrofitService.getRetrofit().create(PresenceApi.class);
        presenceApi.getStudentPresences(studentId, groupId)
                .enqueue(new Callback<StudentPresenceHistoryDTO>() {
                    @Override
                    public void onResponse(Call<StudentPresenceHistoryDTO> call, Response<StudentPresenceHistoryDTO> response) {
                        if(response.body() != null) {
                            setUpLayout(response.body());
                        } else {
                            Toast.makeText(activity, "Nie udało się pobrać historii", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<StudentPresenceHistoryDTO> call, Throwable t) {
                        Toast.makeText(activity, "Nie udało się pobrać historii", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void setUpLayout(StudentPresenceHistoryDTO studentPresenceHistoryDTO) {
        int counter = 0;
        if(studentPresenceHistoryDTO.getPresences().isEmpty()){
            Toast.makeText(activity, "Historia obecności jest pusta", Toast.LENGTH_SHORT).show();
        }
        for(PresenceDTO presenceDTO: studentPresenceHistoryDTO.getPresences()) {
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
            rowLayout.addView(generateDateTexView(presenceDTO));
            rowLayout.addView(generatePresenceTexView(presenceDTO));

            linearLayout.addView(rowLayout);

            descriptionTexView.setText(
                    new StringBuilder()
                            .append(studentPresenceHistoryDTO.getName())
                            .append(" ")
                            .append(studentPresenceHistoryDTO.getLastname())
                            .append(" ")
                            .append(String.valueOf(studentPresenceHistoryDTO.getIndex())).toString()
            );

        }
    }

    private int dpToPx(int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public TextView generateDateTexView(PresenceDTO presenceDTO) {
        TextView textView = new TextView(activity);
        textView.setText(new StringBuilder()
                .append(presenceDTO.getDate().substring(0,10)));
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

    public TextView generatePresenceTexView(PresenceDTO presenceDTO) {
        TextView textView = new TextView(activity);
        textView.setText(new StringBuilder()
                .append(PresenceType.shortToLongPresenceType(presenceDTO.getPresenceType())));
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
        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ActivityHistory.class);
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
