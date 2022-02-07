package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.UserWithCourses;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, AddClassActivity.class);
        intent.putExtra("user_id", 1);
        startActivity(intent);
    }
}
