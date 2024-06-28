package com.example.teaching_app.DatabaseClasses;

public class Student {
    private int id;
    private String name;
    private String lastname;
    private int index;

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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Student(int id, String name, String lastname, int index) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.index = index;
    }
}
