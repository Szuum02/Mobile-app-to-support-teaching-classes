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
import com.example.teachingapp.dtos.LessonPresenceDTO;
import com.example.teachingapp.dtos.StudentDataDTO;
import com.example.teachingapp.enums.PresenceType;
import com.example.teachingapp.retrofit.Api.LessonApi;
import com.example.teachingapp.retrofit.Api.PresenceApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsPresenceTask {
    private final CheckPresence activity;
    private final Long groupId;
    private final Long lessonId;
    private SharedPreferences sharedPreferences;
    private Map<Long, StudentDataDTO> studentsMap;
    private LinearLayout linearLayout;
    private ImageButton activityButton;
    private ImageButton qrButton;
    private ImageButton scanButton;
    private ImageButton returnButton;

    public StudentsPresenceTask(CheckPresence activity, Long groupId, Long lessonId, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.groupId = groupId;
        this.lessonId = lessonId;
        this.sharedPreferences = sharedPreferences;
        this.linearLayout = activity.findViewById(R.id.linearLayout);
        this.activityButton = activity.findViewById(R.id.plus_minus_button);
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

                setUpStudentPresence();
            }
        } else {
            Toast.makeText(activity, "Lista uczniów jest pusta", Toast.LENGTH_SHORT).show();
        }
    }

    public void setUpStudentPresence() {
        RetrofitService retrofitService = new RetrofitService();
        PresenceApi presenceApi = retrofitService.getRetrofit().create(PresenceApi.class);
        presenceApi.getLessonPresence(lessonId)
                .enqueue(new Callback<List<LessonPresenceDTO>>() {

            @Override
            public void onResponse(Call<List<LessonPresenceDTO>> call, Response<List<LessonPresenceDTO>> response) {
                addPresenceToStudent(response.body());
            }

            @Override
            public void onFailure(Call<List<LessonPresenceDTO>> call, Throwable t) {
                Toast.makeText(activity, "Nie pobrano obecności uczniów", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void addPresenceToStudent(List<LessonPresenceDTO> lessonPresenceList) {
        if(lessonPresenceList != null && studentsMap != null) {
            for(LessonPresenceDTO lessonPresenceDTO : lessonPresenceList) {
                if(studentsMap.get(lessonPresenceDTO.getStudentId()) != null)
                    studentsMap.get(lessonPresenceDTO.getStudentId()).setPresence(lessonPresenceDTO.getPresenceType());
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

                if(counter% 2 == 0) {
                    rowLayout.setBackgroundColor(Color.parseColor("#D5D4D4"));
                }
                counter++;


                if(sharedPreferences.getString("left_hand", "off").equals("on")) {
                    addButtons(rowLayout, studentDataDTO);
                    linearLayout.addView(rowLayout);

                    TextView textView = generateTextView(studentDataDTO);
                    rowLayout.addView(textView);
                } else {
                    TextView textView = generateTextView(studentDataDTO);
                    rowLayout.addView(textView);

                    addButtons(rowLayout, studentDataDTO);

                    linearLayout.addView(rowLayout);
                }
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
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        ));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins));
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        return textView;
    }



    public Button createPresencetButton(PresenceType presenceType, StudentDataDTO studentDataDTO) {
        Button button = new Button(activity);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                dpToPx(32),
                dpToPx(32)
        );
        buttonParams.setMarginStart(dpToPx(10));
        buttonParams.setMarginEnd(dpToPx(10));
        button.setLayoutParams(buttonParams);
        setButtonText(button, presenceType);
        button.setAutoSizeTextTypeWithDefaults(Button.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        button.setTextColor(Color.WHITE);
        button.setGravity(Gravity.CENTER);

        button.setTextSize(13);
        if(studentDataDTO.getPresence() == presenceType) {
            setButtonBackground(button, studentDataDTO.getPresence().toString());}
        else {
            button.setBackgroundResource(R.drawable._not_chosen_button);
        }
        return button;
    }

    public void addButtons(LinearLayout rowLayout, StudentDataDTO studentDataDTO) {
        List<Button> buttons = new ArrayList<>();
        Button OButton = createPresencetButton(PresenceType.O, studentDataDTO);
        Button NButton = createPresencetButton(PresenceType.N, studentDataDTO);
        Button SButton = createPresencetButton(PresenceType.S, studentDataDTO);
        Button UButton = createPresencetButton(PresenceType.U, studentDataDTO);

        if(sharedPreferences.getString("left_hand", "off").equals("on")) {
            buttons.add(UButton);
            buttons.add(SButton);
            buttons.add(NButton);
            buttons.add(OButton);

        } else {
            buttons.add(OButton);
            buttons.add(NButton);
            buttons.add(SButton);
            buttons.add(UButton);
        }
        setButtonsOnClickListener(buttons, studentDataDTO);

        for(Button button: buttons){
            rowLayout.addView(button);
        }
    }

    public void setButtonsOnClickListener(List<Button> buttons, StudentDataDTO studentDataDTO) {
        for(Button button: buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setButtonBackground(button, button.getText().toString());
                    for(Button button1 : buttons) {
                        if (button1 != button) {
                            button1.setBackgroundResource(R.drawable._not_chosen_button);
                        }
                    }
                    RetrofitService retrofitService = new RetrofitService();
                    PresenceApi presenceApi = retrofitService.getRetrofit().create(PresenceApi.class);
                    presenceApi.removeAndAddPresence(
                            studentDataDTO.getId(),
                            lessonId,
                            LocalDateTime.now(),
                            PresenceType.stringToPresenceType(button.getText().toString()))
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(studentsMap.get(studentDataDTO.getIndex()) != null)
                                        studentsMap.get(studentDataDTO.getIndex())
                                                .setPresence(PresenceType.stringPresenceType(
                                                        button.getText().toString())
                                                );
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(activity, "Nie udało się zarejestrować obecności", Toast.LENGTH_SHORT).show();
                                    button.setBackgroundResource(R.drawable._not_chosen_button);
                                }
                            });
                }
            });
        }
    }


    public void setButtonText(Button button, PresenceType presenceType) {
        switch (presenceType) {
            case N:
                button.setText("N");
                break;
            case O:
                button.setText("O");
                break;
            case U:
                button.setText("U");
                break;
            case S:
                button.setText("S");
                break;

            default:
                button.setText("");
                break;
        }
    }

    public void setButtonBackground(Button button, String presenceType) {
        switch (presenceType) {
            case "N":
                button.setBackgroundResource(R.drawable._n_button);
                break;

            case "O":
                button.setBackgroundResource(R.drawable._o_button);
                break;

            case "U":
                button.setBackgroundResource(R.drawable._u_button);
                break;

            case "S":
                button.setBackgroundResource(R.drawable._s_button);
                break;
            default:
                button.setBackgroundResource(R.drawable._not_chosen_button);
                break;
        }
    }

    public void setUpButtons() {
        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CheckActivity.class);
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
}