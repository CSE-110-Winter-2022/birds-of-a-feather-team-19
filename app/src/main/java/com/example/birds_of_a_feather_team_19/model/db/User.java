package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Set;
import java.util.TreeSet;

@Entity(tableName = "users")
public class User implements Comparable<User> {

    private AppDatabase db;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id = 0;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "photoURL")
    private String photoURL;

    public User(AppDatabase db, int id, String name, String photoURL) {
        this.db = db;
        this.id = id;
        this.name = name;
        this.photoURL = photoURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    @Override
    public int compareTo(User user) {
        int classmates = 0;
        Set<Course> courses = new TreeSet<>(db.courseDao().getForUser(this.id));
        Set<Course> userCourses = new TreeSet<>(db.courseDao().getForUser(user.getId()));

        for (Course course: courses) {
            if (userCourses.contains(course)) {
                classmates++;
            }
        }

        return classmates;
    }
}
