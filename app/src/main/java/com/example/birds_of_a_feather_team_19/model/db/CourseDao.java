package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface CourseDao {
    @Transaction
    @Query("SELECT * FROM courses WHERE user_id=:userId")
    List<Course> getForUser(int userId);

    @Query("SELECT * FROM courses WHERE id=:id")
    Course get(int id);

    @Query("SELECT * FROM courses WHERE year=:year AND term=:term AND subject=:subject AND number=:number")
    List<Integer> getClassmates(String year, String term, String subject, String number);

    // @Query("SELECT COUNT(*) from courses")
    // int count();

    @Insert
    void insert(Course course);

    @Delete
    void delete(Course course);

    //@Query("SELECT MAX(id) from courses")
    //int maxId();
}
