package com.example.teachingapp.models;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

public class Teacher {
    private Long id;

    private String name;

    private String lastname;

    private User user;

    private Set<Group> groups;

    public Long getId() {
        return id;
    }

    public void setId(Long teacherId) {
        this.id = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
}