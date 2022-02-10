package com.example.birds_of_a_feather_team_19.model.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Course.class, User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase singletonInstance;

    public static AppDatabase singleton(Context context) {
        if(singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, AppDatabase.class, "users.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return singletonInstance;
    }

    public abstract CourseDao courseDao();
    public abstract UserWithCoursesDao userWithCoursesDao();
}
