package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE id=:id")
    User get(String id);

    @Query("SELECT COUNT(*) FROM users")
    int count();

    @Query("SELECT * FROM users WHERE name=:name")
    User getFromName(String name);

    @Query("DELETE FROM users")
    void deleteAll();

    @Insert
    void insert(User user);
}
