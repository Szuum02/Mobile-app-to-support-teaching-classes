package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.ChooseAction;
import com.example.teachingapp.Student.ShowActivity;
import com.example.teachingapp.Student.ShowActivityGroupRanking;
import com.example.teachingapp.Student.ShowActivityPlot;
import com.example.teachingapp.Student.ShowActivityTotalRanking;
import com.example.teachingapp.Teacher.TeacherMainPage;
import com.example.teachingapp.dtos.ActivityPlotDTO;
import com.example.teachingapp.dtos.ActivityRankingDTO;
import com.example.teachingapp.retrofit.Api.ActivityApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

public class RankingActivityTask {
    private ShowActivity activity;
    private long groupId;
    private long studentId;
    private String subject;
    private String nick;
    private SharedPreferences sharedPreferences;
    private Map<Integer, Integer> colorMap = new HashMap<>();

    public RankingActivityTask(ShowActivity activity, long groupId, long studentId, String subject, String nick, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.groupId = groupId;
        this.studentId = studentId;
        this.subject = subject;
        this.nick = nick;
        this.sharedPreferences = sharedPreferences;
        colorMap.put(0, R.drawable.basic_texview);
        colorMap.put(1, R.drawable.basic_texview);
        colorMap.put(2, R.drawable.brown_textview);
        colorMap.put(3, R.drawable.brown_textview);

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
                intent.putExtra("nick", nick);

                activity.startActivity(intent);
            }
        });

        Button groupRankingButton = activity.findViewById(R.id.group_button);
        if (groupRankingButton != null) {
            groupRankingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ShowActivityGroupRanking.class);
                    intent.putExtra("subject", subject);
                    intent.putExtra("student_id", studentId);
                    intent.putExtra("group_id", groupId);
                    intent.putExtra("nick", nick);

                    activity.startActivity(intent);
                }
            });
        }

        Button totalRankingButton = activity.findViewById(R.id.ranking_button);
        if (totalRankingButton != null) {
            totalRankingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ShowActivityTotalRanking.class);
                    intent.putExtra("subject", subject);
                    intent.putExtra("student_id", studentId);
                    intent.putExtra("group_id", groupId);
                    intent.putExtra("nick", nick);

                    activity.startActivity(intent);
                }
            });
        }

        Button plotButton = activity.findViewById(R.id.plot_button);
        if (plotButton != null) {
            plotButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ShowActivityPlot.class);
                    intent.putExtra("subject", subject);
                    intent.putExtra("student_id", studentId);
                    intent.putExtra("group_id", groupId);
                    intent.putExtra("nick", nick);

                    activity.startActivity(intent);
                }
            });
        }
    }

    public void getRanking() {
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);

        activityApi.getRanking(groupId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ActivityRankingDTO>> call,
                                           @NonNull Response<List<ActivityRankingDTO>> response) {
                        createTotalRanking(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ActivityRankingDTO>> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(TeacherMainPage.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

    }

//    public void getStudentHistory() {
//        RetrofitService retrofitService = new RetrofitService();
//        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);
//
//        activityApi.getStudentHistory(studentId, groupId)
//                .enqueue(new Callback<>() {
//                    @Override
//                    public void onResponse(@NonNull Call<List<ActivityDTO>> call,
//                                           @NonNull Response<List<ActivityDTO>> response) {
//                        createStudentHistoryInformation(response.body());
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<List<ActivityDTO>> call,
//                                          @NonNull Throwable t) {
//                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
//                        Logger.getLogger(ChooseGroup.class.getName()).log(Level.SEVERE, "Error occurred", t);
//                    }
//                });
//
//    }

    public void getGroupRanking() {
        RetrofitService retrofitService = new RetrofitService();
        ActivityApi activityApi = retrofitService.getRetrofit().create(ActivityApi.class);

        activityApi.getGroupRanking(groupId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ActivityRankingDTO>> call,
                                           @NonNull Response<List<ActivityRankingDTO>> response) {
                        createGroupRanking(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ActivityRankingDTO>> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(TeacherMainPage.class.getName()).log(Level.SEVERE, "Error occurred", t);
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
                        createActivityPlot(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ActivityPlotDTO>> call,
                                          @NonNull Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(TeacherMainPage.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }

    private void createGroupRanking(List<ActivityRankingDTO> activityRanking) {
        if (activityRanking != null && !activityRanking.isEmpty()) {
            TableLayout rankingTable = activity.findViewById(R.id.activity_table);
            ListIterator<ActivityRankingDTO> iterator = activityRanking.listIterator();
            while (iterator.hasNext()) {
                TableRow row = new TableRow(activity);
                row.setPadding(0, 20, 0, 20);
                int idx = iterator.nextIndex();
                ActivityRankingDTO activityDTO = iterator.next();

                int rowColor = ((activityDTO.getNick().equals(nick)) ? R.drawable.green_textview : colorMap.get(idx % 4));
                row.setBackground(ContextCompat.getDrawable(activity, rowColor));

                row.addView(getTextView(activityDTO.getNick()));
                row.addView(getTextView((activityDTO.getTotalPoints() != null) ? activityDTO.getTotalPoints().toString() : "0"));
                row.addView(getTextView((activityDTO.getTodayPoints() != null) ? activityDTO.getTodayPoints().toString() : ""));

                rankingTable.addView(row);
            }
        } else {
            Toast.makeText(activity, "Brak grup do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private void createTotalRanking(List<ActivityRankingDTO> activityRanking) {
        if (activityRanking != null && !activityRanking.isEmpty()) {
            TableLayout rankingTable = activity.findViewById(R.id.activity_table);
            ListIterator<ActivityRankingDTO> iterator = activityRanking.listIterator();
            while (iterator.hasNext()) {
                TableRow row = new TableRow(activity);
                row.setPadding(0, 20, 0, 20);
                int idx = iterator.nextIndex();
                ActivityRankingDTO activityDTO = iterator.next();

                int rowColor = ((activityDTO.getNick().equals(nick)) ? R.drawable.green_textview : colorMap.get(idx % 4));
                row.setBackground(ContextCompat.getDrawable(activity, rowColor));

                row.addView(getTextView((idx + 1) + "."));
                row.addView(getTextView(activityDTO.getNick()));
                row.addView(getTextView((activityDTO.getTotalPoints() != null) ? activityDTO.getTotalPoints().toString() : "0"));

                rankingTable.addView(row);
            }
        } else {
            Toast.makeText(activity, "Brak grup do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private void createActivityPlot(List<ActivityPlotDTO> activities) {
        if (activities != null && !activities.isEmpty()) {
            GraphView graphView = activity.findViewById(R.id.activity_plot);
            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(getDataPoint(activities));
            graphView.addSeries(series);

            String[] xLabels = new String[activities.size() + 2];

            xLabels[0] = "0";
            for (int i = 0; i < activities.size(); i++) {
                xLabels[i + 1] = String.valueOf(i + 1);
            }
            xLabels[activities.size() + 1] = String.valueOf(activities.size() + 1);

            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
            staticLabelsFormatter.setHorizontalLabels(xLabels);
            graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
//            graphView.getGridLabelRenderer().setNumHorizontalLabels(4);

            graphView.getViewport().setMinX(0);
            graphView.getViewport().setMaxX(activities.size() + 1);
            graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getViewport().setScrollable(true);
            graphView.getViewport().setScrollableY(true);
            series.setShape(PointsGraphSeries.Shape.POINT);
            series.setSize(20);
            series.setColor(R.color.black);
        } else {
            Toast.makeText(activity, "Brak aktywności", Toast.LENGTH_SHORT).show();
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

    private  DataPoint[] getDataPoint(List<ActivityPlotDTO> activities) {
        DataPoint[] dataPoints = new DataPoint[activities.size()];
        for (int i = 0; i < activities.size(); i++) {
            dataPoints[i] = new DataPoint(i + 1, activities.get(i).getPoints());
        }
        return dataPoints;
    }
}
