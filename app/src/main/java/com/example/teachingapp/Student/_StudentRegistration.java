package com.example.teachingapp.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teachingapp.R;
import com.example.teachingapp.Teacher.TeacherMainPage;
import com.example.teachingapp.User._RegistrationName;
import com.example.teachingapp.dtos.StudentDTO;
import com.example.teachingapp.dtos.TeacherDTO;
import com.example.teachingapp.retrofit.Api.UserApi;
import com.example.teachingapp.retrofit.RetrofitService;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class _StudentRegistration extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String name;
    private String lastName;
    private final RetrofitService retrofitService = new RetrofitService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._registration_student_login);
        Intent intent = getIntent();
        if (intent != null) {
            sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
            name = intent.getStringExtra("name");
            lastName = intent.getStringExtra("lastName");
        }
    }

    public void registerHandler(View view) {
        EditText mailText = findViewById(R.id.login);
        EditText passwordText = findViewById(R.id.password);
        EditText confirmPasswordText = findViewById(R.id.confirmPassword);
        EditText nickText = findViewById(R.id.nick);
        EditText indexText = findViewById(R.id.index);

        checkDataAndRegister(mailText, passwordText, confirmPasswordText, nickText, indexText);
    }

    public void returnHandler(View view) {
        Intent intent = new Intent(this, _RegistrationName.class);
        intent.putExtra("name", name);
        intent.putExtra("lastName", lastName);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        startActivity(intent);
    }

    private void checkDataAndRegister(EditText mailText, EditText passwordText,
                                      EditText confirmPasswordText, EditText nickText,
                                      EditText indexText) {
        String mail = mailText.getText().toString();
        String password = passwordText.getText().toString();
        String nick = nickText.getText().toString();
        String indexString = indexText.getText().toString();
        int index = Integer.valueOf(indexString);

        if (validateEmail(mailText) && validateConfirmPassword(passwordText, confirmPasswordText)
                && validateIndex(indexText)) {
            UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
            userApi.checkUniqueValues(mail, index, nick).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    switch (response.body()) {
                        case "ok":
                            registerStudent(mail, password, index, nick);
                            break;
                        case "mail":
                            mailText.setError("Ten mail już istnieje");
                            break;
                        case "nick":
                            nickText.setError("Ten nick już istnieje");
                            break;
                        case "index":
                            indexText.setError("Ten index już istnieje");
                        default:
                            break;
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // todo -> handle error
                }
            });
        }
    }

    private void registerStudent(String mail, String password, int index, String nick) {
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        userApi.addStudent(name, lastName, nick, index, mail, password).enqueue(new Callback<StudentDTO>() {
            @Override
            public void onResponse(Call<StudentDTO> call, Response<StudentDTO> response) {
                goToStudentChooseSubject(response.body());
            }

            @Override
            public void onFailure(Call<StudentDTO> call, Throwable t) {
                // todo -> handle error
            }
        });
    }

    private boolean validateEmail(EditText loginText) {
        Pattern p = Pattern.compile("^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$");
        Matcher m = p.matcher(loginText.getText().toString());
        if (!m.matches()) {
            loginText.setError("Błędny email");
            return false;
        }
        return true;
    }

    private boolean validateConfirmPassword(EditText passwordText, EditText confirmPasswordText) {
        if (!passwordText.getText().toString().equals(
                confirmPasswordText.getText().toString()
        )) {
            confirmPasswordText.setError("Hasła są różne");
            return false;
        }
        return true;
    }

    // TODO -> password pattern?
    private boolean validatePassword() {
        return true;
    }

    private boolean validateIndex(EditText indexText) {
        Pattern p = Pattern.compile("\\d{6}");
        Matcher m = p.matcher(indexText.getText().toString());
        if (!m.matches()) {
            indexText.setError("Album powinien składać się z 6 cyfr");
            return false;
        }
        return true;
    }

    private void goToStudentChooseSubject(StudentDTO studentDTO) {
        Intent intent = new Intent(this, ChooseSubject.class);
        intent.putExtra("student_id", studentDTO.getId());
        intent.putExtra("nick", studentDTO.getNick());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        editor.putString("lessons", gson.toJson(studentDTO.getLessons()));
        editor.apply();
        startActivity(intent);
    }
}
