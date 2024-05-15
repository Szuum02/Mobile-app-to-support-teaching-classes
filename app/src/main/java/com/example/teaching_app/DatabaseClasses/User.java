package com.example.teaching_app.DatabaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Users")
public class User {
    @PrimaryKey
    private int user_id;
    private String mail;
    private String password;
    private int is_student;

    public User(int user_id, String mail, String password, int is_student) {
        this.user_id = user_id;
        this.mail = mail;
        this.password = password;
        this.is_student = is_student;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIs_student() {
        return is_student;
    }

    public void setIs_student(int is_student) {
        this.is_student = is_student;
    }
}
