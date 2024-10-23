package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.Teacher.ChooseLesson;
import com.example.teachingapp.retrofit.Api.TeacherApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsTask {
    private static final String TAG = "FetchGroupsTask";
    private static final String ERROR_MESSAGE = "Cannot fetch groups";

    private ChooseGroup activity;
    private long id;

    public GroupsTask(ChooseGroup activity, long id) {
        this.activity = activity;
        this.id = id;
    }

    public void findAndShowGroups() {
        RetrofitService retrofitService = new RetrofitService();
        TeacherApi teacherApi = retrofitService.getRetrofit().create(TeacherApi.class);

        teacherApi.findAllGroups(id)
                .enqueue(new Callback<List<Object[]>>() {
                    @Override
                    public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
                        List<Object[]> groups = response.body();
                        showGroup(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Object[]>> call, Throwable t) {
                        Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ChooseGroup.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });
    }

    private void showGroup(List<Object[]> groups) {
        if (groups != null && !groups.isEmpty()) {
            LinearLayout layout = activity.findViewById(R.id.linearLayout);
            layout.removeAllViews();
            for (Object[] group : groups) {
                Long groupId = ((Double) group[0]).longValue();
                String subject = (String) group[1];

                Button button = new Button(activity);
                button.setText(subject);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLessons(groupId);
                    }
                });
                layout.addView(button);
            }
        } else {
            Toast.makeText(activity, "Brak grup do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLessons(Long groupId){
        Intent intent = new Intent(activity, ChooseLesson.class);
        intent.putExtra("group", groupId);
        Log.d("GRUPAID", String.valueOf(groupId));
        activity.startActivity(intent);
    }
}
