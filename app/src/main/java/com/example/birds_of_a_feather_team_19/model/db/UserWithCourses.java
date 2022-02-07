package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.birds_of_a_feather_team_19.model.IUser;

import java.util.List;

import com.example.birds_of_a_feather_team_19.model.IUser;

public class UserWithCourses implements IUser {
    @Embedded
    public User user;

    @Relation(parentColumn = "id",
            entityColumn = "user_id",
            entity = Course.class,
            projection = {"course"})

    public List<String> courses;

    @Override
    public int getId() {
        return this.user.userId;
    }

    @Override
    public String getName() {
        return this.user.name;
    }

    @Override
    public List<String> getCourses() {
        return this.courses;
    }


}
