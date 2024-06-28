package org.example.controller;

import jakarta.transaction.Transactional;
import org.example.model.*;
import org.example.reopsitory.GroupRepository;
import org.example.reopsitory.LessonRepository;
import org.example.reopsitory.PresenceRepository;
import org.example.reopsitory.StudentRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/presence")
public class PresenceController {
    private PresenceRepository presenceRepository;
    private LessonRepository lessonRepository;
    private GroupRepository groupRepository;
    private StudentRepository studentRepository;

    public PresenceController(PresenceRepository presenceRepository, LessonRepository lessonRepository, GroupRepository groupRepository, StudentRepository studentRepository) {
        this.presenceRepository = presenceRepository;
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @DeleteMapping("/remove")
    @Transactional
    public void remove(@RequestParam("studentId") long studentId, @RequestParam("lessonId") long lessonId) {
        Student student = studentRepository.findById(studentId);
        Lesson lesson = lessonRepository.findById(lessonId);
        presenceRepository.deletePresenceByLessonAndStudent(lesson, student);
//        return presenceRepository.deletePresenceByLessonAndStudent(lesson, student);
    }

    @PutMapping("/removeAndAdd")
    @Transactional
    @Modifying
    public void removeAndAdd(@RequestParam("studentId") long studentId, @RequestParam("lessonId") long lessonId,
                        @RequestParam("date") String date, @RequestParam("presence") String presenceType) {
        Student student = studentRepository.findById(studentId);
        Lesson lesson = lessonRepository.findById(lessonId);

        presenceRepository.deletePresenceByLessonAndStudent(lesson, student);

        Presence presence = new Presence();
        presence.setDate(LocalDateTime.parse(date));
        presence.setLesson(lesson);
        presence.setStudent(student);
        presence.setPresenceType(Presence.getById(Integer.parseInt(presenceType)));
        presenceRepository.save(presence);
        System.out.println("success");
    }

    @GetMapping("/student/getPresences")
    public List<Object[]> getStudentsPresences(@RequestParam("studentId") long studentId, @RequestParam("groupId") long groupId) {
        return presenceRepository.getStudentPresence(studentId, groupId);
    }
}
