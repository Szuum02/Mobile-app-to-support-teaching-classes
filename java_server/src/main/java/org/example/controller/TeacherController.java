package org.example.controller;

import org.example.model.Group;
import org.example.model.Teacher;
import org.example.model.User;
import org.example.reopsitory.TeacherRepository;
import org.example.reopsitory.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    public TeacherController(TeacherRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/add")
    public ResponseEntity<Teacher> createTeacher(@RequestParam Long id) {
        Teacher teacher = new Teacher();
        teacher.setName("Szymon");
        teacher.setLastname("Fus");
//        teacher.setId(10L);
        User user = userRepository.findById(id).get();
        user.setTeacher(teacher);
        teacher.setUser(user);
        teacherRepository.save(teacher);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/showGroups")
    public List<Object[]> showGroups(@RequestParam long teacherId) {
//        List<Object[]> groups = teacherRepository.getTeachersGroups(teacherId);
        return teacherRepository.getTeachersGroups(teacherId);
    }
}
