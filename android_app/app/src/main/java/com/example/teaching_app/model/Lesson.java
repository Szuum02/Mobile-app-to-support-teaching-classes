package com.example.teaching_app.model;

import java.util.Date;
import java.util.Set;

public class Lesson {
    private Long id;

    private Date date;

    private String classroom;

    private String topic;

    private Group group;

    private Set<Activity> activities;

    private Set<Presence> presences;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    public Set<Presence> getPresences() {
        return presences;
    }

    public void setPresences(Set<Presence> presences) {
        this.presences = presences;
    }
}
