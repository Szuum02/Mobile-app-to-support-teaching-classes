package com.example.teachingapp.Teacher;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.teachingapp.GroupMapKey;
import com.example.teachingapp.R;
import com.example.teachingapp.Tasks.GroupsTask;
import com.example.teachingapp.Tasks.MainPageTeacherTask;
import com.example.teachingapp.User._Login;
import com.example.teachingapp.dtos.AddGroupDTO;
import com.example.teachingapp.dtos.AddLessonDTO;
import com.example.teachingapp.dtos.TeacherDTO;
import com.example.teachingapp.models.Group;
import com.example.teachingapp.models.Lesson;
import com.example.teachingapp.retrofit.Api.TeacherApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherMainPage extends AppCompatActivity {

    private ActivityResultLauncher<Intent> filePickerLauncher;
    private Context activity = this;
    private Long teacherId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._main_page_teacher);
        Intent intent = getIntent();
        if (intent != null) {
            sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            teacherId = getTeacherId(sharedPreferences);
            setUpAddLessonButton();
            MainPageTeacherTask mainPageTeacherTask = new MainPageTeacherTask(this, sharedPreferences);
            mainPageTeacherTask.startTask();
        }
    }

    private void setUpAddLessonButton() {
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            readLessons(uri);
                        }
                    }
                }
        );

        Button addLessonsButton = findViewById(R.id.add_lessons_button);
        addLessonsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                intent = Intent.createChooser(intent, "Wybierz plik CSV");
                filePickerLauncher.launch(intent);
            }
        });

    }

    private void readLessons(Uri uri) {
        try {
            InputStream lessonsFile = getContentResolver().openInputStream(uri);
            InputStreamReader isr = new InputStreamReader(lessonsFile);
            BufferedReader bufferedReader = new BufferedReader(isr);
            getLessonsData(bufferedReader.lines().collect(Collectors.toList()));
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"Nie można odczytać pliku", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateTeacherDTO() {
        RetrofitService retrofitService = new RetrofitService();
        TeacherApi teacherApi = retrofitService.getRetrofit().create(TeacherApi.class);
        teacherApi.teacherLogin(teacherId).enqueue(new Callback<TeacherDTO>() {
            @Override
            public void onResponse(Call<TeacherDTO> call, Response<TeacherDTO> response) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();

                editor.putString("lessons", gson.toJson(response.body().getLessons()));
                editor.putString("teacher_data", gson.toJson(response.body()));
                editor.apply();
            }

            @Override
            public void onFailure(Call<TeacherDTO> call, Throwable t) {
                Toast.makeText(activity, "Nie zaktualizowano lekcji",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLessonsData(List<String> lines) {
        if (lines == null || lines.size() == 0 || lines.size() == 1) {
            Toast.makeText(this, "Brak danych w pliku", Toast.LENGTH_SHORT).show();
            return;
        }
        lines.remove(0);

        Map<GroupMapKey, AddGroupDTO> groups = new HashMap<>();
        for (String line : lines) {
            line = line.replaceAll("\"", "");
            String[] lessonDetails = line.split(";");
            GroupMapKey mapKey = new GroupMapKey(lessonDetails[2], lessonDetails[5]);

            if (!groups.containsKey(mapKey)) {
                AddGroupDTO group = new AddGroupDTO(lessonDetails[2], lessonDetails[3], Integer.parseInt(lessonDetails[5]));
                group.setLessons(new HashSet<>());
                groups.put(mapKey, group);
            }

            AddLessonDTO lesson = new AddLessonDTO(convertDate(lessonDetails[7], lessonDetails[8]), lessonDetails[11]);
            AddGroupDTO group = groups.get(mapKey);
            group.getLessons().add(lesson);
        }

        sendData(new ArrayList<>(groups.values()));
        updateTeacherDTO();

    }

    private void sendData(List<AddGroupDTO> groups) {
        RetrofitService retrofitService = new RetrofitService();
        TeacherApi teacherApi = retrofitService.getRetrofit().create(TeacherApi.class);
        teacherApi.addLessons(groups, teacherId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(activity, "Dodano zajęcia", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "Błąd podczas zapisywania danych", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertDate(String date, String time) {
        if (time.split(":")[0].length() < 2) {
            time = "0" + time;
        }
        return date + "T" + time;
    }

    private Long getTeacherId(SharedPreferences sharedPreferences) {
        String json = sharedPreferences.getString("teacher_data", null);
        if (json == null) {
            return null;
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<TeacherDTO>() {}.getType();
            TeacherDTO teacherDTO = gson.fromJson(json, type);
            return teacherDTO.getId();
        }
    }
}
