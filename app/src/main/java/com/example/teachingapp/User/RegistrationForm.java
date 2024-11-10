package com.example.teachingapp.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.teachingapp.R;
import com.example.teachingapp.Student.ChooseSubject;
import com.example.teachingapp.Student.ShowActivity;
import com.example.teachingapp.Tasks.GroupsTask;
import com.example.teachingapp.Teacher.ChooseGroup;
import com.example.teachingapp.dtos.StudentDTO;
import com.example.teachingapp.dtos.TeacherDTO;
import com.example.teachingapp.retrofit.Api.StudentApi;
import com.example.teachingapp.retrofit.Api.TeacherApi;
import com.example.teachingapp.retrofit.Api.UserApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationForm extends ShowActivity implements AdapterView.OnItemSelectedListener {
    private String type;
    private final RetrofitService retrofitService = new RetrofitService();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_form);
        Intent intent = getIntent();
        if (intent != null) {
            sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            setupSpinner();
        }

//        long studentId;
//        long groupId;
//        String subject;
//        String nick;
//        Intent intent = getIntent();
//        if (intent != null) {
//            studentId = intent.getLongExtra("student_id", 0);
//            groupId = intent.getLongExtra("group_id", 0);
//            subject = intent.getStringExtra("subject");
//            nick = intent.getStringExtra("nick");
//
//            SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
//
//            RankingActivityTask rankingTask = new RankingActivityTask(this, groupId, studentId, subject, nick, sharedPreferences);
//            rankingTask.getPlot();
//        }

    }

    public void register(View view) {
        EditText loginText = findViewById(R.id.MailText);
        EditText passwordText = findViewById(R.id.PasswordText);

        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        if (type.equals("Uczeń")) {
            String login = loginText.getText().toString();
            String password = passwordText.getText().toString();
            userApi.addUser(login, password, true).enqueue(new Callback<Long>() {
                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {
                    addStudent(response.body());
                }

                @Override
                public void onFailure(Call<Long> call, Throwable t) {
                    // TODO -> handle error
                }
            });        }
        else {
            String login = loginText.getText().toString();
            String password = passwordText.getText().toString();
            userApi.addUser(login, password, false).enqueue(new Callback<Long>() {
                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {
                    addTeacher(response.body());
                }

                @Override
                public void onFailure(Call<Long> call, Throwable t) {
                    // TODO -> handle error
                }
            });
        }
    }

    private void addTeacher(Long id) {
        EditText nameText = findViewById(R.id.NameText);
        EditText lastNameText = findViewById(R.id.LastNameText);
        String name = nameText.getText().toString();
        String lastName = lastNameText.getText().toString();

        TeacherApi teacherApi = retrofitService.getRetrofit().create(TeacherApi.class);

        teacherApi.addTeacher(id, name, lastName).enqueue(new Callback<TeacherDTO>() {
            @Override
            public void onResponse(Call<TeacherDTO> call, Response<TeacherDTO> response) {
                goToTeacherChooseGroup(response.body());
            }

            @Override
            public void onFailure(Call<TeacherDTO> call, Throwable t) {
                // TODO -> handle error
            }
        });
    }

    private void goToTeacherChooseGroup(TeacherDTO teacherDTO) {
        Intent intent = new Intent(this, ChooseGroup.class);
        intent.putExtra("teacher_id", teacherDTO.getId());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        editor.putString("lessons", gson.toJson(teacherDTO.getLessons()));
        editor.apply();
        startActivity(intent);
    }

    private void addStudent(Long id) {
        EditText nameText = findViewById(R.id.NameText);
        EditText lastNameText = findViewById(R.id.LastNameText);
        EditText indexText = findViewById(R.id.IndexdText);
        EditText nickText = findViewById(R.id.NickText);
        String name = nameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        Integer index = Integer.valueOf(indexText.getText().toString());
        String nick = nickText.getText().toString();

        StudentApi studentApi = retrofitService.getRetrofit().create(StudentApi.class);

        studentApi.addStudent(id, name, lastName, index, nick).enqueue(new Callback<StudentDTO>() {
            @Override
            public void onResponse(Call<StudentDTO> call, Response<StudentDTO> response) {
                goToStudentChooseSubject(response.body());
            }

            @Override
            public void onFailure(Call<StudentDTO> call, Throwable t) {
                // TODO -> handle error
            }
        });
    }

    private void goToStudentChooseSubject(StudentDTO studentDTO) {
        Intent intent = new Intent(this, ChooseSubject.class);
        intent.putExtra("student_id", studentDTO.getId());
        intent.putExtra("nick", studentDTO.getNick());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        editor.putString("groups", gson.toJson(studentDTO.getGroups()));
        editor.apply();
        startActivity(intent);
    }

    private void setupSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.TypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.type_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        type = parent.getItemAtPosition(pos).toString();
        LinearLayout indexLayout = findViewById(R.id.IndexLayout);
        LinearLayout nickLayout = findViewById(R.id.NickLayout);
        if (type.equals("Uczeń")) {
            indexLayout.setVisibility(View.VISIBLE);
            nickLayout.setVisibility(View.VISIBLE);
        }
        else {
            indexLayout.setVisibility(View.INVISIBLE);
            nickLayout.setVisibility(View.INVISIBLE);
        }
    }

    // TODO -> handle error
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback.
    }
}
