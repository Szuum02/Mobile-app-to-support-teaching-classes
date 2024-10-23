package com.example.teachingapp.Tasks;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.ShowActivityRanking;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.dtos.ActivityDTO;
import com.example.teachingapp.dtos.ActivityPlotDTO;
import com.example.teachingapp.dtos.ActivityRankingDTO;
import com.example.teachingapp.retrofit.Api.ActivityApi;
import com.example.teachingapp.retrofit.RetrofitService;

public class RankingActivityTask {
    private ShowActivityRanking activity;
    private long groupId;
    private long studentId;

    public RankingActivityTask(ShowActivityRanking activity, long groupId, long studentId) {
        this.activity = activity;
        this.groupId = groupId;
        this.studentId = studentId;
    }

    public void getRanking() {
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);

        activityApi.getRanking(groupId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ActivityRankingDTO>> call,
                                           @NonNull Response<List<ActivityRankingDTO>> response) {
                        createRanking(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ActivityRankingDTO>> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ChooseGroup.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

    }

    public void getStudentHistory() {
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);

        activityApi.getStudentHistory(studentId, groupId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ActivityDTO>> call,
                                           @NonNull Response<List<ActivityDTO>> response) {
                        createStudentHistoryInformation(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ActivityDTO>> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ChooseGroup.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

    }

    public void getGroupRanking() {
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);

        activityApi.getGroupRanking(groupId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ActivityRankingDTO>> call,
                                           @NonNull Response<List<ActivityRankingDTO>> response) {
                        createRanking(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ActivityRankingDTO>> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ChooseGroup.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

    }

    public void getPlot() {
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);

        activityApi.getPlot(studentId, groupId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ActivityPlotDTO>> call,
                                           @NonNull Response<List<ActivityPlotDTO>> response) {
                        //TODO zrobic cos z plotem
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ActivityPlotDTO>> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ChooseGroup.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

    }

    private void createRanking(List<ActivityRankingDTO> activityRanking) {
        if (activityRanking != null && !activityRanking.isEmpty()) {
            LinearLayout layout = activity.findViewById(R.id.linearLayout);
            layout.removeAllViews();
            for (int i = 0; i < activityRanking.size(); i++) {
                TextView textView = getTextView(
                        String.format("%d. %s -> %d",
                                i + 1,
                                activityRanking.get(i).getNick(),
                                activityRanking.get(i).getTotalPoints())
                );
                layout.addView(textView);
            }
        } else {
            Toast.makeText(activity, "Brak grup do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private void createStudentHistoryInformation(List<ActivityDTO> activityDto){
        if (activityDto != null && !activityDto.isEmpty()) {
            LinearLayout layout = activity.findViewById(R.id.linearLayout);
            layout.removeAllViews();
            for (int i = 0; i < activityDto.size(); i++) {
                TextView textView = getTextView(
                        String.format("%d. %s -> %d, %s",
                                i + 1,
                                activityDto.get(i).getDate(),
                                activityDto.get(i).getPoints(),
                                activityDto.get(i).getClass())
                );
                layout.addView(textView);
            }
        } else {
            Toast.makeText(activity, "Brak historii do wyświetlenia", Toast.LENGTH_SHORT).show();
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
