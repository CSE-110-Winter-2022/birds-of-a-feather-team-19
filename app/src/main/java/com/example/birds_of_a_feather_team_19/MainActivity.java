package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected RecyclerView usersRecyclerView;
    protected RecyclerView.LayoutManager usersLayoutManager;
    protected UsersViewAdapter usersViewAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (db.usersDao().count() == 0) {
            Intent intent = new Intent(this, UserNameActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);
        setTitle("Birds of a Feather");

        db = AppDatabase.singleton(this);
        List<User> users = db.usersDao().getAll();
        usersRecyclerView = findViewById(R.id.recyclerViewUsers);
        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        usersViewAdapter = new UsersViewAdapter(users);
        usersRecyclerView.setAdapter(usersViewAdapter);
    }
}
