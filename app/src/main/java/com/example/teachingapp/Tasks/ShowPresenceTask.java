package com.example.teachingapp.Tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.ChooseAction;
import com.example.teachingapp.Student.ShowPresence;
import com.example.teachingapp.Teacher.CheckActivity;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.dtos.PresenceDTO;
import com.example.teachingapp.dtos.StudentPresenceHistoryDTO;
import com.example.teachingapp.enums.PresenceType;
import com.example.teachingapp.retrofit.Api.PresenceApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPresenceTask {
    private ShowPresence activity;
    private long groupId;
    private long studentId;
    private String subject;
    private String nick;
    private SharedPreferences sharedPreferences;
    private Map<PresenceType, Integer> isToColorMap = new HashMap<>();


    public ShowPresenceTask(ShowPresence activity, long groupId, long studentId, String subject, String nick, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.groupId = groupId;
        this.studentId = studentId;
        this.subject = subject;
        this.nick = nick;
        this.sharedPreferences = sharedPreferences;
        isToColorMap.put(PresenceType.N, R.drawable.red_textview_right_hand);
        isToColorMap.put(PresenceType.O, R.drawable.green_textview_right_hand);
        isToColorMap.put(PresenceType.S, R.drawable.yellow_textview_right_hand);
        isToColorMap.put(PresenceType.U, R.drawable.blue_textview_right_hand);

        initLayout();
    }

    private void initLayout() {
        TextView subjectText = activity.findViewById(R.id.subject);
        subjectText.setText(subject);

        Button returnButton = activity.findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ChooseAction.class);
                intent.putExtra("subject", subject);
                intent.putExtra("student_id", studentId);
                intent.putExtra("group_id", groupId);
                intent.putExtra("nick",nick);
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
        if (presencesHistory != null) {
            TableLayout table = activity.findViewById(R.id.presence_table);
            for (PresenceDTO presence : presencesHistory.getPresences()) {
                TableRow row = new TableRow(activity);
                row.setPadding(0, 20, 0, 20);

                String dateTime = presence.getDate();
                String date = dateTime.split("T")[0];
                row.addView(getTextView(date));

                PresenceType presenceType = presence.getPresenceType();
                row.addView(getPresence(presenceType));

                int rowColor = isToColorMap.get(presenceType);
                row.setBackground(ContextCompat.getDrawable(activity, rowColor));
                table.addView(row);
            }
        } else {
            Toast.makeText(activity, "Brak obecności do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private TextView getPresence(PresenceType presenceType) {
        switch (presenceType) {
            case N:
                return getTextView("Nieobecność");
            case O:
                return getTextView("Obecność");
            case S:
                return getTextView("Spóźnienie");
            case U:
                return getTextView("Usprawiedliwienie");
            default:
                return getTextView("-");
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
}