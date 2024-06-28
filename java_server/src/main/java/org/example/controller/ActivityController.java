package org.example.controller;

import jakarta.transaction.Transactional;
import org.example.model.Activity;
import org.example.model.Group;
import org.example.model.Lesson;
import org.example.model.Student;
import org.example.reopsitory.ActivityRepository;
import org.example.reopsitory.GroupRepository;
import org.example.reopsitory.LessonRepository;
import org.example.reopsitory.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityRepository activityRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public ActivityController(ActivityRepository activityRepository, LessonRepository lessonRepository,
                              StudentRepository studentRepository, GroupRepository groupRepository) {
        this.activityRepository = activityRepository;
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @PostMapping("/add")
    @Transactional
//    @Modifying
    public int addActivity(@RequestParam("lessonId") long lessonId, @RequestParam("studentId") long studentId,
                           @RequestParam("date") String dateString, @RequestParam("points") int points) {
        Lesson lesson = lessonRepository.findById(lessonId);
        Student student = studentRepository.findById(studentId);
        LocalDateTime dateTime = LocalDateTime.parse(dateString);

        Activity activity = new Activity();
        activity.setLesson(lesson);
        activity.setStudent(student);
        activity.setDate(dateTime);
        activity.setPoints(points);
        activityRepository.save(activity);
        return activityRepository.getStudentsPointsInLesson(student, lesson);
    }

    @GetMapping("/ranking")
    @Transactional
    public List<Object[]> getRanking(@RequestParam("groupId") long groupId) {
        Group group = groupRepository.findById(groupId); //todo -> dodać wyjątek na brak grupy
        String subject = group.getSubject();
        return activityRepository.getRanking(subject);
    }
}
