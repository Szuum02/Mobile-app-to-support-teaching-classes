package com.example.teaching_app.DatabaseDAO;

import com.example.teaching_app.DatabaseClasses.User;
import androidx.room.Dao;
import androidx.room.Query;


import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM Users WHERE mail = :mail AND password = :password")
    User getUser(String mail, String password);
}