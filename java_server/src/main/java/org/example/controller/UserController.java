package org.example.controller;

import org.example.model.User;
import org.example.reopsitory.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/showAll")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/add/test")
    public ResponseEntity<User> test(@RequestParam String mail, @RequestParam String password, @RequestParam boolean isStudent) {
        User user = new User();
        user.setMail(mail);
        user.setPassword(password);
        user.setStudent(isStudent);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

//    @GetMapping("/{mail}")
//    public ResponseEntity<Optional<User>> test(@PathVariable("mail") String mail) {
//        Optional<User> user = userRepository.findById(mail);
//        return ResponseEntity.ok(user);
//    }

    @GetMapping("/teacher/showAll")
    public ResponseEntity<List<User>> findTeachers() {
        List<User> users = userRepository.findAllTeacher();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/students/showAll")
    public ResponseEntity<List<User>> findStudents() {
        List<User> users = userRepository.findAllStudents();
        return ResponseEntity.ok(users);
    }
}
