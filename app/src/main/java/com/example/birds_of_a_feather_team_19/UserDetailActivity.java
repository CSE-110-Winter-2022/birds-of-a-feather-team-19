package com.example.birds_of_a_feather_team_19;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.util.List;

public class UserDetailActivity extends AppCompatActivity {
    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CoursesViewAdapter coursesViewAdapter;
    private AppDatabase db;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        db = AppDatabase.singleton(this);
        int userId = getIntent().getIntExtra("id", 0);
        user = db.userDao().get(userId);
        List<Course> courses = db.courseDao().getForUser(userId);
        // Set the title with the person.
        ((TextView) findViewById(R.id.textViewNameUserDetail)).setText(user.getName());

        coursesRecyclerView = findViewById(R.id.recyclerViewCoursesUserDetail);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);
        coursesViewAdapter = new CoursesViewAdapter(courses);
        coursesRecyclerView.setAdapter(coursesViewAdapter);
    }

    public void onGoBackUserDetailClicked(View view) {
        finish();
    }


}