package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM courses")
    List<Course> getAll();

    @Transaction
    @Query("SELECT * FROM courses WHERE user_id=:userId")
    List<Course> getForUser(String userId);

    @Query("SELECT * FROM courses WHERE id=:id")
    Course get(int id);

    @Query("SELECT * FROM courses WHERE year=:year AND quarter=:quarter AND subject=:subject AND number=:number")
    List<Course> getUsers(String year, String quarter, String subject, String number);

    @Query("DELETE FROM courses")
    void deleteAll();

    @Insert
    void insert(Course course);

}
