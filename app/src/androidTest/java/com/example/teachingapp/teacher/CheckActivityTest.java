package com.example.teachingapp.teacher;

import static android.content.Context.MODE_PRIVATE;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.Tasks.StudentsActivityTask;
import com.example.teachingapp.Teacher.CheckActivity;
import com.example.teachingapp.dtos.LessonPointsDTO;
import com.example.teachingapp.dtos.StudentDataDTO;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class CheckActivityTest {

    @Rule
    public ActivityScenarioRule<CheckActivity> activityScenarioRule =
            new ActivityScenarioRule<>(CheckActivity.class);

    @Test
    public void testAddStudentsToList_correctStudentsMap() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            StudentsActivityTask studentsActivityTask = new StudentsActivityTask(activity,
                    1,
                    1,
                    activity.getSharedPreferences("Settings", MODE_PRIVATE)
            );

            List<Object[]> students = new ArrayList<>();
            students.add(new Object[]{1.0, "John", "Doe", 12345.0});
            students.add(new Object[]{2.0, "Jane", "Smith", 67890.0});

            studentsActivityTask.addStudentsToList(students);
            Map<Long,StudentDataDTO> result = Map.of(
                    1L,
                    new StudentDataDTO(1L, "John", "Doe", 12345L),
                    2L,
                    new StudentDataDTO(2L, "Jane", "Smith", 67890L)
            );

            assertThat(studentsActivityTask.getStudentsMap(), equalTo(result));
        });
    }

    @Test
    public void testAddStudentsToList_emptyStudentsMap() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            StudentsActivityTask studentsActivityTask = new StudentsActivityTask(activity,
                    1,
                    1,
                    activity.getSharedPreferences("Settings", MODE_PRIVATE)
            );

            List<Object[]> students = new ArrayList<>();;

            studentsActivityTask.addStudentsToList(students);
            Map<Long,StudentDataDTO> result = null;

            assertThat(studentsActivityTask.getStudentsMap(), equalTo(result));
        });
    }

    @Test
    public void testAddStudentsToList_nullStudentsMap() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            StudentsActivityTask studentsActivityTask = new StudentsActivityTask(activity,
                    1,
                    1,
                    activity.getSharedPreferences("Settings", MODE_PRIVATE)
            );

            List<Object[]> students = null;

            studentsActivityTask.addStudentsToList(students);
            Map<Long,StudentDataDTO> result = null;

            assertThat(studentsActivityTask.getStudentsMap(), equalTo(result));
        });
    }

    @Test
    public void testAddActivityToStudent_correctActivityList() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            StudentsActivityTask studentsActivityTask = new StudentsActivityTask(activity,
                    1,
                    1,
                    activity.getSharedPreferences("Settings", MODE_PRIVATE)
            );

            List<Object[]> students = new ArrayList<>();
            students.add(new Object[]{1.0, "John", "Doe", 12345.0});
            students.add(new Object[]{2.0, "Jane", "Smith", 67890.0});

            List<LessonPointsDTO> lessonPointsList = List.of(
                    new LessonPointsDTO(1L, 1, 1),
                    new LessonPointsDTO(2L,2,2),
                    new LessonPointsDTO(3L,2,2)
            );

            studentsActivityTask.addStudentsToList(students);
            studentsActivityTask.addActivityToStudent(lessonPointsList);

            StudentDataDTO student1 = new StudentDataDTO(1L, "John", "Doe", 12345L);
            StudentDataDTO student2 = new StudentDataDTO(2L, "Jane", "Smith", 67890L);

            student1.setTodayPoints(1L);
            student1.setAllPoints(1L);
            student2.setAllPoints(2L);
            student2.setTodayPoints(2L);

            Map<Long,StudentDataDTO> result = Map.of(
                    1L,
                    student1,
                    2L,
                    student2
            );

            assertThat(studentsActivityTask.getStudentsMap(), equalTo(result));
        });
    }
}
