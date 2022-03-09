package com.example.birds_of_a_feather_team_19.model.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "photoURL")
    private String photoURL;

    @ColumnInfo(name = "favorite")
    private boolean favorite;

    @Ignore
    public User(String name, String photoURL) {
        this.name = name;
        this.photoURL = photoURL;
        this.favorite = false;
    }

//    @Ignore
    public User(String id, String name, String photoURL) {
        this.id = id;
        this.name = name;
        this.photoURL = photoURL;
        this.favorite = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        return this.id == ((User) o).getId();
    }
}
