package com.example.teachingapp.Tasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teachingapp.MainActivity;
import com.example.teachingapp.R;
import com.example.teachingapp.SettingsActivity;
import com.example.teachingapp.Student.SettingsActivityStudent;
import com.example.teachingapp.Student.ShowActivityGroupRanking;
import com.example.teachingapp.Student.StudentMainPage;
import com.example.teachingapp.Student.StudentScanQR;
import com.example.teachingapp.Student.StudentShowQR;
import com.example.teachingapp.Student._StudentChooseGroup;
import com.example.teachingapp.Teacher.AllGroups;
import com.example.teachingapp.Teacher.ChooseAction;
import com.example.teachingapp.Teacher.TeacherMainPage;
import com.example.teachingapp.dtos.GroupDTO;
import com.example.teachingapp.dtos.LessonDTO;
import com.example.teachingapp.dtos.StudentDTO;
import com.example.teachingapp.dtos.TeacherDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class MainPageStudentTask {
    private StudentMainPage activity;
    private SharedPreferences sharedPreferences;
    private ImageView whiteRectangle;
    private TextView showAllClassesTextView;
    private TextView helloTextView;
    private TextView subjectTextView;
    private Button logOutButton;
    private Button showQrButton;
    private Button scanQrButton;
    private Button settingsButton;
    private StudentDTO studentDTO;
    private LessonDTO upcomingLesson;

    public MainPageStudentTask(StudentMainPage activity, SharedPreferences sharedPreferences) {
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
        this.whiteRectangle = activity.findViewById(R.id.white_rectangle);
        this.showAllClassesTextView = activity.findViewById(R.id.show_all_classes);
        this.helloTextView = activity.findViewById(R.id.welcome_texView);
        this.logOutButton = activity.findViewById(R.id.log_off_button);
        this.showQrButton = activity.findViewById(R.id.show_qr_button);
        this.scanQrButton = activity.findViewById(R.id.scan_qr_button);
        this.subjectTextView = activity.findViewById(R.id.subject_textView);
        this.settingsButton = activity.findViewById(R.id.settings_button);
        this.studentDTO = getStudentDto();
        this.upcomingLesson = null;
    }

    public void startTask() {
        setUpLogOutButton();
        setUpShowQrButton();
        setUpScanQrButton();
        setUpHelloTextView();
        setUpSubjectTextView();
        setUpSettingsButton();
        setUpShowAllClassesTextView();
        setUpWhiteRectangle();

    }

    private void setUpLogOutButton() {
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            }
        });
    }

    private void setUpShowQrButton() {
        showQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, StudentShowQR.class);
                intent.putExtra("student_id", studentDTO.getId());
                activity.startActivity(intent);
            }
        });
    }

    private void setUpScanQrButton() {
        scanQrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, StudentScanQR.class);
                intent.putExtra("student_id", studentDTO.getId());
                activity.startActivity(intent);
            }
        });
    }

    private void setUpHelloTextView() {
        if (studentDTO == null) {
            helloTextView.setText("Witaj");
        } else {
            helloTextView.setText(
                    new StringBuilder()
                            .append("Witaj ")
                            .append(studentDTO.getName())
                            .append(" ")
                            .append(studentDTO.getLastname()).toString());
        }
    }

    private void setUpSubjectTextView() {
        LessonDTO upcomingLesson = findUpcomingClasses();
        if (upcomingLesson == null) {
            subjectTextView.setText("Brak najbliższych zajęć");
        } else {

            Spannable spannableString = setUpcomingLessonText();

            subjectTextView.setText(spannableString);

        }
    }

    private Spannable setUpcomingLessonText() {
        String topic = upcomingLesson.getTopic();

        String date = getDateFromData(upcomingLesson);

        String classRoom = String.valueOf(upcomingLesson.getClassroom());

        String combinedText = topic + "\n" + date + "\n" + classRoom;


        SpannableString spannableString = new SpannableString(combinedText);

        int topicEnd = topic.length();
        spannableString.setSpan(
                new RelativeSizeSpan(1.17f),
                0,
                topicEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );


        int dateEnd = topicEnd + date.length() + 1;
        spannableString.setSpan(
                new ForegroundColorSpan(Color.parseColor("#555555")),
                topicEnd + 1,
                dateEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannableString.setSpan(
                new ForegroundColorSpan(Color.parseColor("#555555")),
                dateEnd + 1,
                combinedText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return spannableString;
    }

    private String getDateFromData(LessonDTO upcomingLesson) {
        LocalDateTime localDateTime = upcomingLesson.getDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return localDateTime.getDayOfMonth()
                + "-" + addZeroToString(String.valueOf(localDateTime.getMonth().getValue()))
                + "-" + localDateTime.getYear()
                + "\n" + addZeroToString(String.valueOf(localDateTime.getHour()))
                + ":" + addZeroToString(String.valueOf(localDateTime.getMinute()));
    }

    private String addZeroToString(String string) {
        if(string.length() < 2){
            return "0" + string;
        }
        return string;
    }
    private LessonDTO findUpcomingClasses() {
        if (studentDTO == null) {
            return null;
        }
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime closestDate = LocalDateTime.now().plusDays(366);

        for (List<LessonDTO> lessonList : studentDTO.getLessons().values()) {
            for (LessonDTO lesson : lessonList) {
                LocalDateTime lessonDate  = lesson
                        .getDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                if (lessonDate.isAfter(currentDate) && lessonDate.isBefore(closestDate)) {
                    closestDate = lessonDate;
                    upcomingLesson = lesson;
                }
            }
        }
        return upcomingLesson;
    }

    private StudentDTO getStudentDto() {
        String json = sharedPreferences.getString("student_data", null);
        if (json == null) {
            return null;
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<StudentDTO>() {}.getType();
            return gson.fromJson(json, type);
        }
    }

    public void setUpShowAllClassesTextView() {
        showAllClassesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, _StudentChooseGroup.class);
                activity.startActivity(intent);
            }
        });
    }

    private void setUpWhiteRectangle() {
        if(upcomingLesson != null) {
            whiteRectangle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ShowActivityGroupRanking.class);
                    intent.putExtra("group_id", upcomingLesson.getGroupId());
                    intent.putExtra("student_id", studentDTO.getId());
                    intent.putExtra("nick", studentDTO.getNick());
                    activity.startActivity(intent);
                }
            });
        }
    }

    public void setUpSettingsButton() {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SettingsActivityStudent.class);
                intent.putExtra("student_id", studentDTO.getId());
                activity.startActivity(intent);
            }
        });
    }
}
