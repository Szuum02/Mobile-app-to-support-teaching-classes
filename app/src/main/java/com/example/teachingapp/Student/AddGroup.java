package com.example.teachingapp.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.AddStudentToGroupTask;
import com.example.teachingapp.dtos.LessonDTO;
import com.example.teachingapp.retrofit.Api.StudentApi;
import com.example.teachingapp.retrofit.RetrofitService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGroup extends AppCompatActivity {
    private long studentId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout._add_student_to_group);
        Intent intent = getIntent();
        if (intent != null) {
            studentId = intent.getLongExtra("student_id", 0);
            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            AddStudentToGroupTask task = new AddStudentToGroupTask(this, studentId, sharedPreferences);
            task.startTask();
        }
    }
}
