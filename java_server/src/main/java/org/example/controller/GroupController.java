package org.example.controller;

import org.example.model.Student;
import org.example.reopsitory.GroupRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {
    private GroupRepository groupRepository;

    public GroupController(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @GetMapping("/showStudents")
    public List<Object[]> showStudents(@RequestParam("groupStudentId") long groupId) {
        return groupRepository.getStudentsByGroup(groupId);
    }

    @GetMapping("/lessons/showAll")
    public List<Object[]> showLessons(@RequestParam("groupId") long groupId) {
        return groupRepository.getLessons(groupId);
    }
}
