package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "courses")
public class Course implements Comparable<Course> {
    private static final Map<String, Integer> quarterMap = new HashMap<String, Integer>() {{
        put("spring", 0);
        put("summer session 1", 1);
        put("summer session 2", 2);
        put("special summer session", 3);
        put("fall", 4);
        put("winter", 5);
    }};

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "owner_id")
    private int ownerId;

    @ColumnInfo(name = "year")
    private String year;

    @ColumnInfo(name = "quarter")
    private String quarter;

    @ColumnInfo(name = "subject")
    private String subject;

    @ColumnInfo(name = "number")
    private String number;

    public Course(int userId, String year, String quarter, String subject, String number, int ownerId) {
        this.userId = userId;
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.number = number;
        this.ownerId = ownerId;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getYear() { return year; }

    public void setYear(String year) { this.year = year; }

    public String getQuarter() { return quarter; }

    public void setQuarter(String quarter) { this.quarter = quarter; }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getNumber() { return number; }

    public void setNumber(String number) { this.number = number; }

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
            return Integer.parseInt(this.year) < Integer.parseInt(course.year) ? -1 : 1;
        } else if (quarterMap.get(this.quarter) != quarterMap.get(course.getQuarter())) {
            return quarterMap.get(this.quarter) < quarterMap.get(course.getQuarter()) ? -1 : 1;
        } else if (!this.subject.equals(course.getSubject())) {
            return this.subject.compareTo(course.getSubject());
        } else {
            return this.number.compareTo(course.getNumber());
        }
    }
}
