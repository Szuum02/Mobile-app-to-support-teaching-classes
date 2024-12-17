package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.ChooseAction;
import com.example.teachingapp.Teacher.ChooseStudent;
import com.example.teachingapp.Teacher.PresenceHistory;
import com.example.teachingapp.Teacher.TeacherMainPage;
import com.example.teachingapp.dtos.StudentDataDTO;
import com.example.teachingapp.retrofit.Api.LessonApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseStudentTask {

    private final ChooseStudent activity;
    private final Long lessonId;
    private final Long groupId;
    private final String groupCode;
    private LinearLayout linearLayout;
    private Button returnButton;

    public ChooseStudentTask(ChooseStudent activity, Long groupId, Long lessonId, String groupCode) {
        this.activity = activity;
        this.lessonId = lessonId;
        this.groupId = groupId;
        this.groupCode = groupCode;
        this.linearLayout = activity.findViewById(R.id.linearLayout);
        returnButton = activity.findViewById(R.id.return_button);
    }

    public void startTask() {
        getStudentsList();
        setUpButtons();
    }

    public void getStudentsList() {
        RetrofitService retrofitService = new RetrofitService();
        LessonApi lessonApi = retrofitService.getRetrofit().create(LessonApi.class);
        lessonApi.getStudents(groupId)
                .enqueue(new Callback<List<Object[]>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Object[]>> call,
                                           @NonNull Response<List<Object[]>> response) {
                        if (response.body() != null) {
                            setUpLayout(mapStudentList(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Object[]>> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(activity, "Nie udało się pobrać listy uczniów", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(
                                TeacherMainPage.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }

    public void setUpLayout(List<StudentDataDTO> studentDataDTOList) {
        int counter = 0;
        if (studentDataDTOList != null) {
            for (StudentDataDTO studentDataDTO : studentDataDTOList) {
                LinearLayout rowLayout = new LinearLayout(activity);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dpToPx(52)
                ));
                rowLayout.setGravity(Gravity.CENTER);

                TextView textView = generateTextView(studentDataDTO);
                rowLayout.addView(textView);

                if(counter % 2 == 0) {
                    rowLayout.setBackgroundColor(Color.parseColor("#D5D4D4"));
                }
                counter++;
                linearLayout.addView(rowLayout);
            }
        }
    }

    private int dpToPx(int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public TextView generateTextView(StudentDataDTO studentDataDTO) {
        TextView textView = new TextView(activity);
        textView.setText(new StringBuilder()
                .append(studentDataDTO.getName())
                .append(" ")
                .append(studentDataDTO.getLastname())
                .append(" ")
                .toString());
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins));
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PresenceHistory.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("student_id", studentDataDTO.getId());
                intent.putExtra("group_code", groupCode);
                activity.startActivity(intent);
            }
        });
        return textView;
    }

    private List<StudentDataDTO> mapStudentList(List<Object[]> results) {
        List<StudentDataDTO> studentList = new ArrayList<>();
        if (results != null && !results.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                studentList = results.stream()
                        .map(row -> new StudentDataDTO(
                                ((Double) row[0]).longValue(),
                                (String) row[1],
                                (String) row[2],
                                ((Double) row[3]).longValue()
                        ))
                        .collect(Collectors.toList());
            }
        } else {
            Toast.makeText(activity, "Lista uczniów jest pusta", Toast.LENGTH_SHORT).show();
        }
        return studentList;
    }

    public void setUpButtons() {
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ChooseAction.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("lesson_id", lessonId);
                intent.putExtra("group_code", groupCode);
                activity.startActivity(intent);
            }
        });
    }
}
