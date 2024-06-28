package org.example.controller;

import jakarta.transaction.Transactional;
import org.example.model.Student;
import org.example.model.Teacher;
import org.example.model.User;
import org.example.reopsitory.StudentRepository;
import org.example.reopsitory.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public StudentController(UserRepository userRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/add")
    public ResponseEntity<Student> createTeacher(@RequestParam Long id) {
        Student student = new Student();
        student.setName("Kacper");
        student.setLastname("Urbański");
        student.setIndex(123456);
        student.setNick("My_nick");
        User user = userRepository.findById(id).get();
        user.setStudent(student);
        student.setUser(user);
        studentRepository.save(student);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/showGroups")
    @Transactional
    public List<Object[]> getGroups(@RequestParam("studentId") long studentId) {
        return studentRepository.getStudentsGroup(studentId);
    }
}
