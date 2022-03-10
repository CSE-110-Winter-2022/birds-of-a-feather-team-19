package com.example.birds_of_a_feather_team_19.model.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

@Entity(tableName = "users")
@TypeConverters({Converters.class})
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "photoURL")
    private String photoURL;

    @ColumnInfo(name = "session_id")
    private HashSet<Integer> sessionIds;
  
    @ColumnInfo(name = "favorite")
    private boolean favorite;

    @Ignore
    public User(String name, String photoURL) {
        this.name = name;
        this.photoURL = photoURL;
        this.favorite = false;
    }

    public User(String id, String name, String photoURL) {
        this.id = id;
        this.name = name;
        this.photoURL = photoURL;
        this.sessionIds = new HashSet<>();
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

    public void setSessionIds(HashSet<Integer> sessionIds) {
        this.sessionIds = sessionIds;
    }

    public HashSet<Integer> getSessionIds() {
        return sessionIds;
    }

    public void addSessionId(int sessionId) {
        this.sessionIds.add(sessionId);
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        return this.id.equals(((User) o).getId());
    }
}
