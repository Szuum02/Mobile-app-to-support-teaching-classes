package com.example.teachingapp.models;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {
    private Long id;
    private String name;
    private String lastname;
    private Integer index;
    private String nick;
    private User user;
    private Set<Group> groups;
    private Set<Activity> activities;
    private Set<Presence> presences;
}
