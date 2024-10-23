package com.example.teachingapp.models;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Teacher {
    private Long id;
    private String name;
    private String lastname;
    private User user;
    private Set<Group> groups;
}
