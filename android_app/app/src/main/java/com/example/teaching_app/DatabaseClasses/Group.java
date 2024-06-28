package com.example.teaching_app.DatabaseClasses;

public class Group {
    private int id;
    private String name;
    private int teacher_id;

    public Group(int id, String name, int teacher_id) {
        this.id = id;
        this.name = name;
        this.teacher_id = teacher_id;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }
}
