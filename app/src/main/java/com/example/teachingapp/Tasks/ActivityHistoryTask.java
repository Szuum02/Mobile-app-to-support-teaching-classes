package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.ActivityHistory;
import com.example.teachingapp.Teacher.TeacherMainPage;
import com.example.teachingapp.dtos.ActivityDTO;
import com.example.teachingapp.dtos.StudentHistoryDTO;
import com.example.teachingapp.retrofit.Api.ActivityApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHistoryTask {
    private final ActivityHistory activity;
    private final long studentId;
    private final long groupId;
    private int count;
    Map<Integer, Boolean> isToColorMap = new HashMap<>();

    public ActivityHistoryTask(ActivityHistory activity, long studentId, long groupId) {
        this.activity = activity;
        this.studentId = studentId;
        this.groupId = groupId;
        count = 0;
        isToColorMap.put(0, true);
        isToColorMap.put(1, false);
        isToColorMap.put(2, false);
        isToColorMap.put(3, true);

        setUpLayoutButtons();
    }

    public void showHistory() {
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);

        activityApi.getStudentHistory(studentId, groupId).enqueue(new Callback<StudentHistoryDTO>() {
            @Override
            public void onResponse(Call<StudentHistoryDTO> call, Response<StudentHistoryDTO> response) {
                createTable(response.body());
                setTextView(response.body());
            }

            @Override
            public void onFailure(Call<StudentHistoryDTO> call, Throwable t) {
                Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                Logger.getLogger(TeacherMainPage.class.getName()).log(Level.SEVERE, "Error occurred", t);
            }
        });
    }

    public void createTable(StudentHistoryDTO studentHistoryDTO) {
        LinearLayout linearLayout = activity.findViewById(R.id.dynamic_layout);

        TableLayout tableLayout = new TableLayout(activity);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow headerRow = new TableRow(activity);
        headerRow.setBackgroundColor(Color.parseColor("#AA7A51"));
        String[] headers = {"Data", "Czas", "Punkty"};

        for (String header : headers) {
            TextView textView = new TextView(activity);
            textView.setText(header);
            textView.setPadding(16, 16, 16, 16);
            textView.setGravity(Gravity.CENTER);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0,
                    TableRow.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            textView.setLayoutParams(params);

            headerRow.addView(textView);
        }
        tableLayout.addView(headerRow);

        List<ActivityDTO> activityDTOList = studentHistoryDTO.getActivities();
        for (ActivityDTO activityDTO : activityDTOList) {
            TableRow tableRow = new TableRow(activity);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            String date = activityDTO.getDate().substring(8, 10) + "-" + activityDTO.getDate().substring(5, 7);
            String time = activityDTO.getDate().substring(11, 19);
            String[] data = {date, time, String.valueOf(activityDTO.getPoints())};

            GradientDrawable border = new GradientDrawable();
            if(isToColorMap.get(count%4)){
                border.setColor(Color.parseColor("#AA7A51"));
            }
            border.setStroke(4, Color.BLACK);

            for (String cellData : data) {

                TextView textView = new TextView(activity);
                textView.setText(cellData);
                textView.setPadding(16, 16, 16, 16);
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(border);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.WRAP_CONTENT);
                params.weight = 1;
                textView.setLayoutParams(params);
                textView.setTextColor(Color.BLACK);
                tableRow.addView(textView);
            }

            tableLayout.addView(tableRow);
            count++;
        }

        linearLayout.addView(tableLayout);
    }


    public void setTextView(StudentHistoryDTO studentHistoryDTO) {
        TextView textView = activity.findViewById(R.id.studentHistoryTextView);

        String text = studentHistoryDTO.getName() + " " + studentHistoryDTO.getLastname() + " " + studentHistoryDTO.getIndex();

        textView.setText(text);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void setUpLayoutButtons() {
        Button timeTableButton = activity.findViewById(R.id.timeTable_button);

        timeTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TeacherMainPage.class);
                activity.startActivity(intent);
            }
        });
    }

}
