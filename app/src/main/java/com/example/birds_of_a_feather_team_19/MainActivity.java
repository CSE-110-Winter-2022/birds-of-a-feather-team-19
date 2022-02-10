package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected RecyclerView usersRecyclerView;
    protected RecyclerView.LayoutManager usersLayoutManager;
    protected UsersViewAdapter usersViewAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.singleton(this);
        if (db.usersDao().count() == 0) {
            Intent intent = new Intent(this, UserNameActivity.class);
            startActivity(intent);
        }
        setTitle("Birds of a Feather");

        List<Course> myCourses = db.courseDao().getForUser(0);
        List<User> users = db.usersDao().getAll();
        usersRecyclerView = findViewById(R.id.recyclerViewUsers);

        for (User user : users) {
            boolean haveSameClass = false;
            List<Course> studentCourses = db.courseDao().getForUser(user.getId());
            for(Course course : studentCourses){
                boolean courseSame = false;
                for(Course myCourse : myCourses){
                    if(myCourse == course){
                        haveSameClass = true;
                        courseSame = true;
                        break;
                    }
                }
                if(courseSame == false){
                    db.courseDao().delete(course);
                }
            }
            if(haveSameClass == false){
                db.usersDao().delete(user);
            }
        }

        db = AppDatabase.singleton(this);
        List<User> matchedUsers = db.usersDao().getAll();
        usersRecyclerView = findViewById(R.id.recyclerViewUsers);
        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        usersViewAdapter = new UsersViewAdapter(matchedUsers);
        usersRecyclerView.setAdapter(usersViewAdapter);
    }
}
