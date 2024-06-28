package org.example.controller;

import org.example.model.User;
import org.example.reopsitory.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/showAll")
    public List<User> getAllUsers() {
//        List<User> users = userRepository.findAll();
        return userRepository.findAll();
    }

    @PostMapping("/mail")
    public User findUserByMail(@RequestParam String mail, @RequestParam String password) {
        User user = userRepository.findUserByMail(mail);
        System.out.println(user.getMail() + " " + user.getPassword() + " " + user.getId());
        return user;
    }

    @GetMapping("/add/test")
    public User test(@RequestParam String mail, @RequestParam String password, @RequestParam boolean isStudent) {
        User user = new User();
        user.setMail(mail);
        user.setPassword(password);
        user.setIsStudent(isStudent);
        userRepository.save(user);
        return user;
    }

//    @GetMapping("/{mail}")
//    public ResponseEntity<Optional<User>> test(@PathVariable("mail") String mail) {
//        Optional<User> user = userRepository.findById(mail);
//        return ResponseEntity.ok(user);
//    }

    @GetMapping("/teacher/showAll")
    public List<User> findTeachers() {
//        List<User> users = userRepository.findAllTeacher();
        return userRepository.findAllTeacher();
    }

    @GetMapping("/students/showAll")
    public List<User> findStudents() {
//        List<User> users = userRepository.findAllStudents();
        return userRepository.findAllStudents();
    }
}
