package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.ChooseAction;
import com.example.teachingapp.Student.ShowActivityGroupRanking;
import com.example.teachingapp.Student.ShowActivityTotalRanking;
import com.example.teachingapp.Student.ShowPresence;
import com.example.teachingapp.Student.StudentMainPage;
import com.example.teachingapp.dtos.ActivityRankingDTO;
import com.example.teachingapp.dtos.PresenceDTO;
import com.example.teachingapp.dtos.StudentPresenceHistoryDTO;
import com.example.teachingapp.enums.PresenceType;
import com.example.teachingapp.retrofit.Api.PresenceApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPresenceTask {
    private ShowPresence activity;
    private long groupId;
    private long studentId;
    private String nick;
    private SharedPreferences sharedPreferences;
    private Map<PresenceType, Integer> isToColorMap = new HashMap<>();
    private ImageButton groupRankingButton;
    private ImageButton totalRankingButton;
    private ImageButton plotButton;
    private ImageButton presenceButton;
    private ImageButton returnButton;
    private LinearLayout linearLayout;

    public ShowPresenceTask(ShowPresence activity, long groupId, long studentId, String nick, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.groupId = groupId;
        this.studentId = studentId;
        this.nick = nick;
        this.sharedPreferences = sharedPreferences;
//        isToColorMap.put(PresenceType.N, R.drawable.red_textview_right_hand);
//        isToColorMap.put(PresenceType.O, R.drawable.green_textview_right_hand);
//        isToColorMap.put(PresenceType.S, R.drawable.yellow_textview_right_hand);
//        isToColorMap.put(PresenceType.U, R.drawable.blue_textview_right_hand);
        linearLayout = activity.findViewById(R.id.linearLayout);
        groupRankingButton = activity.findViewById(R.id.three_people_button);
        totalRankingButton = activity.findViewById(R.id.five_people_button);
        plotButton = activity.findViewById(R.id.plot_button);
        presenceButton = activity.findViewById(R.id.calendar_button);
        returnButton = activity.findViewById(R.id.return_button);
        setupButtons();
    }

    private void setupButtons() {
        groupRankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ShowActivityGroupRanking.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("student_id", studentId);
                intent.putExtra("nick", nick);
                activity.startActivity(intent);
            }
        });

        totalRankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ShowActivityTotalRanking.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("student_id", studentId);
                intent.putExtra("nick", nick);
                activity.startActivity(intent);
            }
        });

//        scanButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(activity, TeacherScanQR.class);
//                intent.putExtra("group_id", groupId);
//                intent.putExtra("lesson_id", lessonId);
//                activity.startActivity(intent);
//            }
//        });
//
        presenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ShowPresence.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("student_id", studentId);
                intent.putExtra("nick", nick);
                activity.startActivity(intent);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, StudentMainPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
            }
        });
    }

    public void findAndShowPresences() {
        RetrofitService retrofitService = new RetrofitService();
        PresenceApi presenceApi = retrofitService.getRetrofit().create(PresenceApi.class);

        presenceApi.getStudentPresences(studentId, groupId)
                .enqueue(new Callback<StudentPresenceHistoryDTO>() {
                    @Override
                    public void onResponse(Call<StudentPresenceHistoryDTO> call, Response<StudentPresenceHistoryDTO> response) {
                        showPresences(response.body());
                    }

                    @Override
                    public void onFailure(Call<StudentPresenceHistoryDTO> call, Throwable t) {
                        int x = 0;
                        // TODO -> handle error
                    }
                });

    }

    private void showPresences(StudentPresenceHistoryDTO presencesHistory) {
        if (presencesHistory != null &&!presencesHistory.getPresences().isEmpty()) {
            ListIterator<PresenceDTO> iterator = presencesHistory.getPresences().listIterator();
            while (iterator.hasNext()) {
                int idx = iterator.nextIndex();
                PresenceDTO presenceDTO = iterator.next();

                LinearLayout rowLayout = new LinearLayout(activity);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dpToPx(52)
                ));
                rowLayout.setGravity(Gravity.CENTER);

                TextView dateText = generateTextView(convertDate(presenceDTO.getDate()));
                TextView presenceTypeText = generateTextView(getPresence(presenceDTO.getPresenceType()));

                rowLayout.addView(dateText);
                rowLayout.addView(presenceTypeText);

                if(idx % 2 == 0) {
                    rowLayout.setBackgroundColor(Color.parseColor("#D5D4D4"));
                }
                linearLayout.addView(rowLayout);
            }
        } else {
            Toast.makeText(activity, "Brak obecności do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertDate(String date) {
        return date.split("T")[0];
    }

    private String getPresence(PresenceType presenceType) {
        switch (presenceType) {
            case N:
                return "Nieobecność";
            case O:
                return "Obecność";
            case S:
                return "Spóźnienie";
            case U:
                return "Usprawiedliwienie";
            default:
                return "-";
        }
    }

    private TextView getTextView(String text) {
        TextView textView = new TextView(activity);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        return textView;
    }

    private LinearLayout preparePresenceLinearLayout() {
        LinearLayout layout = new LinearLayout(activity);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(0, 10, 0, 10);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        return layout;
    }

    public TextView generateTextView(String text) {
        TextView textView = new TextView(activity);
        textView.setText(text);
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

    private int dpToPx(int dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}