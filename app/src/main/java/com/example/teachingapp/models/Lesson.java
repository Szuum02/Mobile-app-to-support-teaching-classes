package com.example.teachingapp.models;

import java.util.Date;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Lesson {
    private Long id;
    private Date date;
    private String classroom;
    private String topic;
    private Group group;
    private Set<Activity> activities;
    private Set<Presence> presences;
}
