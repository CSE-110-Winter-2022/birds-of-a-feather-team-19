package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "photoURL")
    private String photoURL;

    @Ignore
    public User(String name, String photoURL) {
        this.name = name;
        this.photoURL = photoURL;
    }

//    @Ignore
    public User(int id, String name, String photoURL) {
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
    public boolean equals(Object o) {
        return this.id == ((User) o).getId();
    }
}
