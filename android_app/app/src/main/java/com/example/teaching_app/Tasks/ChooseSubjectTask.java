package com.example.teaching_app.Tasks;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.teaching_app.R;
import com.example.teaching_app.Student.ChooseAction;
import com.example.teaching_app.Student.ChooseSubject;
import com.example.teaching_app.Teacher.ChooseGroup;
import com.example.teaching_app.Teacher.ChooseLesson;
import com.example.teaching_app.retrofit.RetrofitService;
import com.example.teaching_app.retrofit.StudentApi;
import com.example.teaching_app.retrofit.TeacherApi;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseSubjectTask {
    private ChooseSubject activity;
    private long studentId;

    public ChooseSubjectTask(ChooseSubject activity, long studentId) {
        this.activity = activity;
        this.studentId = studentId;
    }

    public void findAndShowGroups() {
        RetrofitService retrofitService = new RetrofitService();
        StudentApi studentApi = retrofitService.getRetrofit().create(StudentApi.class);

        studentApi.findAllGroups(studentId)
                .enqueue(new Callback<List<Object[]>>() {
                    @Override
                    public void onResponse(Call<List<Object[]>> call, Response<List<Object[]>> response) {
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
                        showActions(groupId);
                    }
                });
                layout.addView(button);
            }
        } else {
            Toast.makeText(activity, "Brak grup do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private void showActions(Long groupId){
        Intent intent = new Intent(activity, ChooseAction.class);
        intent.putExtra("group", groupId);
        intent.putExtra("student", studentId);
        Log.d("GRUPAID", String.valueOf(groupId));
        activity.startActivity(intent);
    }
}
