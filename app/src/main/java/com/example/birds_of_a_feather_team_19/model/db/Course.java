package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int courseId;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "course")
    public String course;

    public Course(int courseId, int userId, String course) {
        this.courseId = courseId;
        this.userId = userId;
        this.course = course;
    }
}
