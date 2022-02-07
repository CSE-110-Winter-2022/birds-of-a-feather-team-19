package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @ColumnInfo(name = "id")
    public int userId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "photoUrl")
    public String photoUrl;

//    public User(int userId, String name, String photoUrl) {
//        this.userId = userId;
//        this.name = name;
//        this.photoUrl = photoUrl;
//    }
}
