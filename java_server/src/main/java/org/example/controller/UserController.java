package org.example.controller;

import org.example.model.User;
import org.example.reopsitory.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/showAll")
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println(userRepository.findAll().size());
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/add/test")
    public ResponseEntity<User> test() {
        User user = new User();
        user.setUserId(10L);
        user.setMail("nowy@example.com");
        user.setPassword("strongPassword");
        user.setStudent(false);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/{mail}")
    public ResponseEntity<Optional<User>> test(@PathVariable("mail") String mail) {
        Optional<User> user = userRepository.findById(mail);
        return ResponseEntity.ok(user);
    }
}
