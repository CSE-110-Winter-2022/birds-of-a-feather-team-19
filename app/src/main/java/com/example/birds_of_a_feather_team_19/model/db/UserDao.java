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
    User get(int id);

    @Query("SELECT COUNT(*) from courses")
    int count();

    @Insert
    void insert(User user);

//    @Insert
//    void insert(IUser user);
}
