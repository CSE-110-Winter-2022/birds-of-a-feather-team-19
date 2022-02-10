package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id = 0;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "year")
    private String year;

    @ColumnInfo(name = "term")
    private String term;

    @ColumnInfo(name = "subject")
    private String subject;

    @ColumnInfo(name = "number")
    private String number;

    public Course(int userId, String year, String term, String subject, String number) {
        this.userId = userId;
        this.year = year;
        this.term = term;
        this.subject = subject;
        this.number = number;
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

    public String getYear() { return this.year; }

    public void setYear(String year) { this.year = year; }

    public String getTerm() { return this.term; }

    public void setTerm(String term) { this.term = term; }

    public String getSubject() { return this.subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getNumber() { return this.number; }

    public void setNumber(String number) { this.number = number; }
}
