package org.example.controller;

import org.example.reopsitory.LessonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    private LessonRepository lessonRepository;

    public LessonController(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/showStudents")
    public List<Object[]> getStudents(@RequestParam("lessonId") long lessonId) {
        return lessonRepository.findStudentsByLesson(lessonId);
    }
}
