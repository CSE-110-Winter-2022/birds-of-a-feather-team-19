package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "courses")
public class Course implements Comparable<Course> {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "quarter")
    private int quarter;

    @ColumnInfo(name = "subject")
    private String subject;

    @ColumnInfo(name = "number")
    private String number;

    @ColumnInfo(name = "size")
    private double size;

    public Course(String userId, int year, int quarter, String subject, String number, double size) {
        this.userId = userId;
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.number = number;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public int getYear() { return year; }

    public int getQuarter() { return quarter; }

    public String getSubject() { return subject; }

    public String getNumber() { return number; }

    public double getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        return (year == ((Course) o).getYear() &&
                quarter == ((Course) o).getQuarter() &&
                subject.equals(((Course) o).getSubject()) &&
                number.equals(((Course) o).getNumber()));
    }

    @Override
    public int compareTo(Course course) {
        if (!(this.year == course.getYear())) {
            return this.year - course.getYear();
        } else if (!(this.quarter == course.getQuarter())) {
            return this.quarter - course.getQuarter();
        } else if (!this.subject.equals(course.getSubject())) {
            return this.subject.compareTo(course.getSubject());
        } else {
            return this.number.compareTo(course.getNumber());
        }
    }
}
