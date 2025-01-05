package com.example.teachingapp.teacher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.teachingapp.Tasks.ChooseStudentTask;
import com.example.teachingapp.Teacher.ChooseStudent;
import com.example.teachingapp.dtos.StudentDataDTO;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ChooseStudentTest {

    @Rule
    public ActivityScenarioRule<ChooseStudent> activityScenarioRule =
            new ActivityScenarioRule<>(ChooseStudent.class);

    @Test
    public void testSetUpLayout_notEmptyStudentDataDTOList() {
        List<StudentDataDTO> studentDataDTOList = List.of(
                new StudentDataDTO(100L, "Jan", "Kowal", 100L),
                new StudentDataDTO(101L, "Jan", "Kowal", 101L),
                new StudentDataDTO(102L, "Jan", "Kowal", 102L)
        );

        activityScenarioRule.getScenario().onActivity(activity -> {
            ChooseStudentTask chooseStudentTask = new ChooseStudentTask(
                    activity, 1L, 1L, "CODE");

            chooseStudentTask.setUpLayout(studentDataDTOList);

            assertThat(chooseStudentTask.getLinearLayout().getChildCount(), is(3));
        });
    }

    @Test
    public void testSetUpLayout_emptyStudentDataDTOList() {
        List<StudentDataDTO> studentDataDTOList = List.of();

        activityScenarioRule.getScenario().onActivity(activity -> {
            ChooseStudentTask chooseStudentTask = new ChooseStudentTask(
                    activity, 1L, 1L, "CODE");

            chooseStudentTask.setUpLayout(studentDataDTOList);

            assertThat(chooseStudentTask.getLinearLayout().getChildCount(), is(0));
        });
    }

    @Test
    public void testSetUpLayout_nullStudentDataDTOList() {
        List<StudentDataDTO> studentDataDTOList = null;

        activityScenarioRule.getScenario().onActivity(activity -> {
            ChooseStudentTask chooseStudentTask = new ChooseStudentTask(
                    activity, 1L, 1L, "CODE");

            chooseStudentTask.setUpLayout(studentDataDTOList);

            assertThat(chooseStudentTask.getLinearLayout().getChildCount(), is(0));
        });
    }

    @Test
    public void testMapStudentList_correctStudentsList() {
        List<StudentDataDTO> studentDataDTOList = null;

        activityScenarioRule.getScenario().onActivity(activity -> {
            ChooseStudentTask chooseStudentTask = new ChooseStudentTask(
                    activity, 1L, 1L, "CODE");

            List<Object[]> students = new ArrayList<>();
            students.add(new Object[]{1.0, "John", "Doe", 12345.0});
            students.add(new Object[]{2.0, "Jane", "Smith", 67890.0});

            List<StudentDataDTO> result = List.of(
                    new StudentDataDTO(1L, "John", "Doe", 12345L),
                    new StudentDataDTO(2L, "Jane", "Smith", 67890L)
            );

            assertThat(chooseStudentTask.mapStudentList(students), equalTo(result));
        });
    }

    @Test
    public void testMapStudentList_emptyStudentsList() {
        List<StudentDataDTO> studentDataDTOList = null;

        activityScenarioRule.getScenario().onActivity(activity -> {
            ChooseStudentTask chooseStudentTask = new ChooseStudentTask(
                    activity, 1L, 1L, "CODE");

            List<Object[]> students = new ArrayList<>();

            List<StudentDataDTO> result = List.of();

            assertThat(chooseStudentTask.mapStudentList(students), equalTo(result));
        });
    }

    @Test
    public void testMapStudentList_nullStudentsList() {
        List<StudentDataDTO> studentDataDTOList = null;

        activityScenarioRule.getScenario().onActivity(activity -> {
            ChooseStudentTask chooseStudentTask = new ChooseStudentTask(
                    activity, 1L, 1L, "CODE");

            List<Object[]> students = null;

            List<StudentDataDTO> result = List.of();

            assertThat(chooseStudentTask.mapStudentList(students), equalTo(result));
        });
    }
}
