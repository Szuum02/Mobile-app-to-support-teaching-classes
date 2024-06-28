package com.example.teaching_app.DAOs;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.teaching_app.DatabaseClasses.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM Users WHERE mail = :mail AND password = :password")
    User findUserByMail(String mail, String password);

    @Query("SELECT * FROM Users")
    List<User> findAllUsers();
}
