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
    private int userId;

    @ColumnInfo(name = "year")
    private String year;

    @ColumnInfo(name = "quarter")
    private String quarter;

    @ColumnInfo(name = "subject")
    private String subject;

    @ColumnInfo(name = "number")
    private String number;

    @ColumnInfo(name = "size")
    private String size;

    private static final Map<String, Integer> quarterMap = new HashMap<String, Integer>() {{
        put("winter", 0);
        put("spring", 1);
        put("summer session 1", 2);
        put("summer session 2", 3);
        put("special summer session", 4);
        put("fall", 5);
    }};

    public Course(int userId, String year, String quarter, String subject, String number, String size) {
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

    public int getUserId() {
        return userId;
    }

    public String getYear() { return year; }

    public String getQuarter() { return quarter; }

    public String getSubject() { return subject; }

    public String getNumber() { return number; }

    public String getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        return (year.equals(((Course) o).getYear()) &&
                quarter.equals(((Course) o).getQuarter()) &&
                subject.equals(((Course) o).getSubject()) &&
                number.equals(((Course) o).getNumber()));
    }

    @Override
    public int compareTo(Course course) {
        if (!this.year.equals(course.getYear())) {
            return Integer.parseInt(this.year) - Integer.parseInt(course.year);
        } else if (!this.quarter.equals(course.getQuarter())) {
            return quarterMap.get(this.quarter) - quarterMap.get(course.getQuarter());
        } else if (!this.subject.equals(course.getSubject())) {
            return this.subject.compareTo(course.getSubject());
        } else {
            return Integer.parseInt(this.number) - Integer.parseInt(course.getNumber());
        }
    }
}
