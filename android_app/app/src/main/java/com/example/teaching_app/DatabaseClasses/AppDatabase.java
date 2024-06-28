package com.example.teaching_app.DatabaseClasses;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.teaching_app.DAOs.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
