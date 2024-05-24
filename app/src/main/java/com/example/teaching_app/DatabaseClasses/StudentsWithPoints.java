package com.example.teaching_app.DatabaseClasses;

public class StudentsWithPoints extends Student{

    private int points;
    public StudentsWithPoints(int id, String name, String lastname, int index, int points) {
        super(id, name, lastname, index);
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
