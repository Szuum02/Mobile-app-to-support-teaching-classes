package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.ChooseAction;
import com.example.teachingapp.Student.ChooseSubject;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.retrofit.Api.StudentApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChooseSubjectTask {
    private final ChooseSubject activity;
    private final long studentId;

    public ChooseSubjectTask(ChooseSubject activity, long studentId) {
        this.activity = activity;
        this.studentId = studentId;
    }

    public void findAndShowGroups() {
        RetrofitService retrofitService = new RetrofitService();
        StudentApi studentApi = retrofitService.getRetrofit().create(StudentApi.class);

        studentApi.findAllGroups(studentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Object[]>> call,
                                   @NonNull Response<List<Object[]>> response) {
                showGroup(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Object[]>> call, @NonNull Throwable t) {
                Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                Logger.getLogger(ChooseGroup.class.getName())
                        .log(Level.SEVERE, "Error occurred", t);
            }
        });
    }

    private void showGroup(List<Object[]> groups) {
        if (groups != null && !groups.isEmpty()) {
            LinearLayout layout = activity.findViewById(R.id.linearLayout);
            layout.removeAllViews();
            for (Object[] group : groups) {
                Button button = new Button(activity);
                button.setText(group[1].toString());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showActions((Long) group[0]);
                    }
                });
                layout.addView(button);
            }
        } else {
            Toast.makeText(activity, "Brak grup do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private void showActions(Long groupId){
        Intent intent = new Intent(activity, ChooseAction.class)
                .putExtra("group", groupId)
                .putExtra("student", studentId);

        Log.d("GRUPAID", String.valueOf(groupId));
        activity.startActivity(intent);
    }
}
