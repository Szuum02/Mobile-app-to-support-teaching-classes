package com.example.teachingapp;

import static android.content.Context.MODE_PRIVATE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.Tasks.StudentsPresenceTask;
import com.example.teachingapp.Teacher.CheckPresence;
import com.example.teachingapp.dtos.LessonPresenceDTO;
import com.example.teachingapp.dtos.StudentDataDTO;
import com.example.teachingapp.enums.PresenceType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class CheckPresenceTest {

    @Rule
    public ActivityScenarioRule<CheckPresence> activityScenarioRule =
            new ActivityScenarioRule<>(CheckPresence.class);

    @Test
    public void testAddStudentsToList_correctStudentsMap() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            StudentsPresenceTask studentsPresenceTask = new StudentsPresenceTask(activity,
                    1L,
                    1L,
                    activity.getSharedPreferences("Settings", MODE_PRIVATE)
            );

            List<Object[]> students = new ArrayList<>();
            students.add(new Object[]{1.0, "John", "Doe", 12345.0});
            students.add(new Object[]{2.0, "Jane", "Smith", 67890.0});

            studentsPresenceTask.addStudentsToList(students);
            Map<Long, StudentDataDTO> result = Map.of(
                    1L,
                    new StudentDataDTO(1L, "John", "Doe", 12345L),
                    2L,
                    new StudentDataDTO(2L, "Jane", "Smith", 67890L)
            );

            assertThat(studentsPresenceTask.getStudentsMap(), equalTo(result));
        });
    }

    @Test
    public void testAddStudentsToList_emptyStudentsMap() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            StudentsPresenceTask studentsPresenceTask = new StudentsPresenceTask(activity,
                    1L,
                    1L,
                    activity.getSharedPreferences("Settings", MODE_PRIVATE)
            );

            List<Object[]> students = new ArrayList<>();

            studentsPresenceTask.addStudentsToList(students);

            assertThat(studentsPresenceTask.getStudentsMap(), equalTo(null));
        });
    }

    @Test
    public void testAddStudentsToList_nullStudentsMap() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            StudentsPresenceTask studentsPresenceTask = new StudentsPresenceTask(activity,
                    1L,
                    1L,
                    activity.getSharedPreferences("Settings", MODE_PRIVATE)
            );

            List<Object[]> students = null;

            studentsPresenceTask.addStudentsToList(students);

            assertThat(studentsPresenceTask.getStudentsMap(), equalTo(null));
        });
    }

    @Test
    public void testAddAPresenceToStudent_correctActivityList() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            StudentsPresenceTask studentsPresenceTask = new StudentsPresenceTask(activity,
                    1L,
                    1L,
                    activity.getSharedPreferences("Settings", MODE_PRIVATE)
            );
            List<Object[]> students = new ArrayList<>();
            students.add(new Object[]{1.0, "John", "Doe", 12345.0});
            students.add(new Object[]{2.0, "Jane", "Smith", 67890.0});

            List<LessonPresenceDTO> lessonPresence = List.of(
                    new LessonPresenceDTO(1L, PresenceType.N),
                    new LessonPresenceDTO(2L, PresenceType.O),
                    new LessonPresenceDTO(3L, PresenceType.S)
            );

            studentsPresenceTask.addStudentsToList(students);
            studentsPresenceTask.addPresenceToStudent(lessonPresence);

            StudentDataDTO student1 = new StudentDataDTO(1L, "John", "Doe", 12345L);
            StudentDataDTO student2 = new StudentDataDTO(2L, "Jane", "Smith", 67890L);

            student1.setPresence(PresenceType.N);
            student2.setPresence(PresenceType.O);

            Map<Long,StudentDataDTO> result = Map.of(
                    1L,
                    student1,
                    2L,
                    student2
            );

            assertThat(studentsPresenceTask.getStudentsMap(), equalTo(result));
        });
    }
}
