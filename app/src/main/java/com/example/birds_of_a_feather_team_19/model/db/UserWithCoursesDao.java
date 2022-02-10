package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.birds_of_a_feather_team_19.model.IUser;

import java.util.List;

@Dao
public interface UserWithCoursesDao {
    @Transaction
    @Query("SELECT * FROM users")
    List<UserWithCourses> getAll();

    @Query("SELECT * FROM users WHERE id=:id")
    UserWithCourses get(int id);

    @Query("SELECT COUNT(*) from courses")
    int count();

//    @Insert
//    void insert(IUser user);
}
