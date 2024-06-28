package com.example.teaching_app.Tasks;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.teaching_app.R;
import com.example.teaching_app.Student.ShowActivityRanking;
import com.example.teaching_app.Teacher.ChooseGroup;
import com.example.teaching_app.retrofit.ActivityApi;
import com.example.teaching_app.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowRankingTask {
    private ShowActivityRanking activity;
    private long groupId;
    private long studentId;

    public ShowRankingTask(ShowActivityRanking activity, long groupId, long studentId) {
        this.activity = activity;
        this.groupId = groupId;
        this.studentId = studentId;
    }

    public void showRanking() {
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);

        activityApi.getRanking(groupId)
                .enqueue(new Callback<List<Object[]>>() {
                    @Override
                    public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
                        createRanking(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Object[]>> call, Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ChooseGroup.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

    }

    private void createRanking(List<Object[]> students) {
        if (students != null && !students.isEmpty()) {
            LinearLayout layout = activity.findViewById(R.id.linearLayout);
            layout.removeAllViews();
            for (int i = 0; i < students.size(); i++) {
                String fullName = (String) students.get(i)[0];
                int points = ((Double) students.get(i)[1]).intValue();
                TextView textView = getTextView(String.format("%d. %s -> %d", i + 1, fullName, points));
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
