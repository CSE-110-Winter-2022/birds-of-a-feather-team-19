package com.example.birds_of_a_feather_team_19.model.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SessionDao {
    @Query("SELECT * FROM sessions")
    List<Session> getAll();

    @Query("SELECT * FROM sessions WHERE id=:id")
    Session get(int id);

    @Query("SELECT * FROM sessions WHERE name=:sessionName")
    Session get(String sessionName);

    @Transaction
    @Query("SELECT * FROM users WHERE session_id=:id")
    List<User> getUsersInSession(int id);

    @Insert
    void insert(Session session);

    @Update
    void update(Session session);

    @Query("DELETE FROM courses")
    void deleteAll();
}
