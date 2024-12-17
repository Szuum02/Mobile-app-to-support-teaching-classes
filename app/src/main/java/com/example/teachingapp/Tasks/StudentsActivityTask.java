package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.CheckActivity;
import com.example.teachingapp.Teacher.CheckPresence;
import com.example.teachingapp.Teacher.ChooseAction;
import com.example.teachingapp.Teacher.ShowQR;
import com.example.teachingapp.Teacher.TeacherMainPage;
import com.example.teachingapp.Teacher.TeacherScanQR;
import com.example.teachingapp.dtos.LessonPointsDTO;
import com.example.teachingapp.dtos.StudentDataDTO;
import com.example.teachingapp.retrofit.Api.ActivityApi;
import com.example.teachingapp.retrofit.Api.LessonApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsActivityTask {
    private final CheckActivity activity;
    private final long groupId;
    private final long lessonId;
    private final String groupCode;
    private SharedPreferences sharedPreferences;
    private Map<Long, StudentDataDTO> studentsMap;
    private ImageButton presenceButton;
    private ImageButton qrButton;
    private ImageButton scanButton;
    private ImageButton returnButton;
    private LinearLayout linearLayout;
    public StudentsActivityTask(CheckActivity activity, long groupId, long lessonId, String groupCode, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.groupId = groupId;
        this.groupCode = groupCode;
        this.sharedPreferences = sharedPreferences;
        this.lessonId = lessonId;
        this.linearLayout = activity.findViewById(R.id.linearLayout);
        this.presenceButton = activity.findViewById(R.id.calendar_button);
        this.qrButton = activity.findViewById(R.id.show_qr_code_button);
        this.scanButton = activity.findViewById(R.id.scan_qr_code_button);
        this.returnButton = activity.findViewById(R.id.return_button);
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
                            activity.runOnUiThread(() -> {
                                addStudentsToList(response.body());
                            });
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

    public void addStudentsToList(List<Object[]> results) {
        if (results != null && !results.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                studentsMap = results.stream()
                        .collect(Collectors.toMap(
                                row -> ((Double) row[0]).longValue(),
                                row -> new StudentDataDTO(
                                        ((Double) row[0]).longValue(),
                                        (String) row[1],
                                        (String) row[2],
                                        ((Double) row[3]).longValue()
                                )
                        ));

                setUpStudentActivity();
            }
        } else {
            Toast.makeText(activity, "Lista uczniów jest pusta", Toast.LENGTH_SHORT).show();
        }
    }


    public void setUpStudentActivity() {
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);
        activityApi.showLessonActivity(groupId)
                .enqueue(new Callback<List<LessonPointsDTO>>() {
                    @Override
                    public void onResponse(Call<List<LessonPointsDTO>> call, Response<List<LessonPointsDTO>> response) {
                        addActivityToStudent(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<LessonPointsDTO>> call, Throwable t) {

                    }
                });
    }

    public void addActivityToStudent(List<LessonPointsDTO> lessonPointsList) {
        if(lessonPointsList != null && studentsMap != null) {
            for(LessonPointsDTO lessonPointsDTO : lessonPointsList) {
                if(studentsMap.get(lessonPointsDTO.getStudentId()) != null ) {
                    studentsMap.get(lessonPointsDTO.getStudentId()).setAllPoints(lessonPointsDTO.getTotalPoints());
                }
                if(studentsMap.get(lessonPointsDTO.getStudentId()) != null && lessonPointsDTO.getTotalPoints() != 0) {
                    studentsMap.get(lessonPointsDTO.getStudentId()).setTodayPoints(lessonPointsDTO.getTodayPoints());
                }

            }
            setUpLayout();
        }
    }

    public void setUpLayout() {
        int counter = 0;
        if(studentsMap != null) {
            for (StudentDataDTO studentDataDTO : studentsMap.values()) {
                LinearLayout rowLayout = new LinearLayout(activity);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dpToPx(52)
                ));
                rowLayout.setGravity(Gravity.CENTER);

                TextView textView = generateTextView(studentDataDTO);
                TextView todayPoints = generateTodayPointsTextView(studentDataDTO);
                TextView allPoint = generateAllPointsTextView(studentDataDTO);

                if(sharedPreferences.getString("left_hand", "off").equals("on")) {
                    rowLayout.addView(addLayoutWithButtons(todayPoints, allPoint, studentDataDTO));
                    rowLayout.addView(allPoint);
                    rowLayout.addView(todayPoints);
                    rowLayout.addView(textView);

                } else {
                    rowLayout.addView(textView);
                    rowLayout.addView(todayPoints);
                    rowLayout.addView(allPoint);
                    rowLayout.addView(addLayoutWithButtons(todayPoints, allPoint, studentDataDTO));
                }

                if(counter% 2 == 0) {
                    rowLayout.setBackgroundColor(Color.parseColor("#D5D4D4"));
                }
                counter++;

                linearLayout.addView(rowLayout);

            }
        }
    }

    public TextView generateTodayPointsTextView(StudentDataDTO studentDataDTO) {
        TextView textView = new TextView(activity);
        if(studentDataDTO.getTodayPoints() != null) {
            if (studentDataDTO.getTodayPoints() > 0) {
                textView.setText("+" + String.valueOf(studentDataDTO.getTodayPoints()));
            } else {
                textView.setText(String.valueOf(studentDataDTO.getTodayPoints()));
            }
        }

        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                13
        ));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins));
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    public LinearLayout addLayoutWithButtons(TextView todayPoint, TextView allPoints, StudentDataDTO studentDataDTO) {
        LinearLayout rowLayout = new LinearLayout(activity);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                32
        ));
        rowLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                dpToPx(36),
                dpToPx(36)
        );
        buttonParams.setMarginEnd(dpToPx(24));

        Button pluseButton = new Button(activity);
        Button minusButton = new Button(activity);

        pluseButton.setBackgroundResource(R.drawable._o_button);
        pluseButton.setTextSize(15);
        pluseButton.setTextColor(Color.WHITE);
        pluseButton.setGravity(Gravity.CENTER);
        pluseButton.setText("+");
        pluseButton.setLayoutParams(buttonParams);

        minusButton.setBackgroundResource(R.drawable._n_button);
        minusButton.setTextSize(15);
        minusButton.setTextColor(Color.WHITE);
        minusButton.setGravity(Gravity.CENTER);
        minusButton.setText("-");
        minusButton.setLayoutParams(buttonParams);


        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitService retrofitService = new RetrofitService();
                ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);
                activityApi.addActivity(lessonId, studentDataDTO.getId(), LocalDateTime.now(), -1).enqueue(new Callback<Integer>() {
                    @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                setTextPointsAllPoints(allPoints, -1);
                                setTextPointTodayPointss(todayPoint, -1);
                                studentDataDTO.setAllPoints(studentDataDTO.getAllPoints()-1);
                                if(studentDataDTO.getTodayPoints() == null) {
                                    studentDataDTO.setTodayPoints(-1L);
                                } else {
                                    studentDataDTO.setTodayPoints(studentDataDTO.getTodayPoints()-1);
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Toast.makeText(activity, "Nie udało się zarejestrować aktywności", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        pluseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitService retrofitService = new RetrofitService();
                ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);
                activityApi.addActivity(lessonId, studentDataDTO.getId(), LocalDateTime.now(), 1)
                        .enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                setTextPointsAllPoints(allPoints, 1);
                                setTextPointTodayPointss(todayPoint, 1);
                                studentDataDTO.setAllPoints(studentDataDTO.getAllPoints()+1);
                                if(studentDataDTO.getTodayPoints() == null) {
                                    studentDataDTO.setTodayPoints(1L);
                                } else {
                                    studentDataDTO.setTodayPoints(studentDataDTO.getTodayPoints()+1);
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Toast.makeText(activity, "Nie udało się zarejestrować aktywności", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        if(sharedPreferences.getString("left_hand", "off").equals("on")) {
            rowLayout.addView(pluseButton);
            rowLayout.addView(minusButton);
        } else {
            rowLayout.addView(minusButton);
            rowLayout.addView(pluseButton);
        }


        return rowLayout;
    }

    public TextView generateAllPointsTextView(StudentDataDTO studentDataDTO) {
        TextView textView = new TextView(activity);
        if(studentDataDTO.getAllPoints() == null) {
            textView.setText(String.valueOf(0));
        } else {
            textView.setText(String.valueOf(studentDataDTO.getAllPoints()));
        }
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                13
        ));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins));
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        return textView;
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
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                39
        ));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins));
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    public void setUpButtons() {
        presenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CheckPresence.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("lesson_id", lessonId);
                activity.startActivity(intent);
            }
        });

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ShowQR.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("lesson_id", lessonId);
                activity.startActivity(intent);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TeacherScanQR.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("lesson_id", lessonId);
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

    public void setTextPointsAllPoints(TextView textView, Integer value) {
        if(textView.getText() != null) {
            Integer number;
            if (textView.getText().toString().equals("")) {
                number = value;
            } else {
                number = Integer.valueOf(textView.getText().toString()) + value;
            }
            if (number > 0) {
                textView.setText(String.valueOf(number));
            } else if (number == 0) {
                textView.setText("0");
            } else {
                textView.setText(String.valueOf(number));
            }
        }
    }

    public void setTextPointTodayPointss(TextView textView, Integer value) {
        if(textView.getText() != null) {
            Integer number;
            if (textView.getText().toString().equals("")) {
                number = value;
            } else {
                number = Integer.valueOf(textView.getText().toString()) + value;
            }
            if (number > 0) {
                textView.setText("+" + String.valueOf(number));
            } else if (number <= 0) {
                textView.setText(String.valueOf(number));
            } else {
                textView.setText("");

            }
        }
    }
}
