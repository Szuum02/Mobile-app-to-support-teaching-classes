package com.example.teachingapp.Tasks;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.ShowPresence;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.retrofit.Api.PresenceApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPresenceTask {
    private ShowPresence activity;
    private long groupId;
    private long studentId;

    public ShowPresenceTask(ShowPresence activity, long groupId, long studentId) {
        this.activity = activity;
        this.groupId = groupId;
        this.studentId = studentId;
    }

    public void findAndShowPresences() {
        RetrofitService retrofitService = new RetrofitService();
        PresenceApi presenceApi = retrofitService.getRetrofit().create(PresenceApi.class);

        presenceApi.getPresences(studentId, groupId)
                .enqueue(new Callback<List<Object[]>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Object[]>> call,
                                           @NonNull Response<List<Object[]>> response) {
                        showPresences(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Object[]>> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ChooseGroup.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

    }

    private void showPresences(List<Object[]> presences) {
        if (presences != null && !presences.isEmpty()) {
            LinearLayout layout = activity.findViewById(R.id.linearLayout);
            layout.removeAllViews();
            for (Object[] presence : presences) {
                String dateTime = (String) presence[0];
                String date = dateTime.split("T")[0];
                String presenceType = (String) presence[1];

                TextView textView = getTextView(date + " " + presenceType);
                layout.addView(textView);
            }
        } else {
            Toast.makeText(activity, "Brak grup do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private TextView getTextView(String text) {
        TextView textView = new TextView(activity);
        textView.setText(text);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        return textView;
    }
}