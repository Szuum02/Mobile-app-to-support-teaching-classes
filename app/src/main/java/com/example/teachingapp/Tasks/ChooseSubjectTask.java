package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.ChooseAction;
import com.example.teachingapp.Student.ChooseSubject;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.Teacher.PresenceOrActivity;
import com.example.teachingapp.dtos.GroupDTO;
import com.example.teachingapp.retrofit.Api.StudentApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChooseSubjectTask {
    private final ChooseSubject activity;
    private final Long studentId;
    private String nick;
    SharedPreferences sharedPreferences;

    public ChooseSubjectTask(ChooseSubject activity, Long studentId, String nick, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.studentId = studentId;
        this.nick = nick;
        this.sharedPreferences = sharedPreferences;
    }

    public void findAndShowSubjects() {
        List<GroupDTO> groups = getGroups();
        showSubjects(groups);
    }

    private List<GroupDTO> getGroups() {
        String json = sharedPreferences.getString("groups", null);
        if (json == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<GroupDTO>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void showSubjects(List<GroupDTO> groups) {
        if (groups != null && !groups.isEmpty()) {
            addSubjects(groups);
        } else {
            Toast.makeText(activity, "Brak przedmiotów do wyświetlenia", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSubjects(List<GroupDTO> groups) {
        LinearLayout layout = activity.findViewById(R.id.linearLayout);
        layout.removeAllViews();

        for (GroupDTO group : groups) {
            Long groupId = group.getGroupId();
            String subject = group.getSubject();

            Button button = new Button(activity);
            button.setText(subject);
            button.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_background));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(0, 0, 0, 16);
            button.setLayoutParams(params);

            button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOptions(groupId, subject);
                }
            });

            layout.addView(button);
        }
    }

    private void showOptions(Long groupId, String subject) {
        Intent intent = new Intent(activity, ChooseAction.class);
        intent.putExtra("group_id", groupId);
        intent.putExtra("student_id", studentId);
        intent.putExtra("subject", subject);
        intent.putExtra("nick", nick);
        activity.startActivity(intent);
    }

//    private void showActions(Long groupId){
//        Intent intent = new Intent(activity, ChooseAction.class);
//        intent.putExtra("group", groupId);
//        intent.putExtra("student", studentId);
//
//        Log.d("GRUPAID", String.valueOf(groupId));
//        activity.startActivity(intent);
//    }
}
