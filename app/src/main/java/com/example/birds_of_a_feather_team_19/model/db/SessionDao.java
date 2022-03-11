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

    @Transaction
    @Query("SELECT * FROM users WHERE session_id=:sessionId")
    List<User> getUsersInSession(int sessionId);

    @Query("SELECT * FROM sessions WHERE id=:id")
    Session get(int id);

    @Query("SELECT * FROM sessions WHERE session_name=:sessionName")
    Session get(String sessionName);

    @Query("DELETE FROM courses")
    void deleteAll();

    @Insert
    void insert(Session session);

    @Query("UPDATE sessions SET session_name=:newName WHERE id=:id")
    void update(int id, String newName);
}
