package com.example.birds_of_a_feather_team_19;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserDetailActivity extends AppCompatActivity {
    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CoursesViewAdapter coursesViewAdapter;
    private AppDatabase db;
    private User user;
    private static final String TAG = "BoF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        db = AppDatabase.singleton(this);
        user = db.userDao().get(getIntent().getIntExtra("id", 0));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Bitmap> future = (executor.submit(() -> BitmapFactory.decodeStream(new URL(user.getPhotoURL()).openStream())));
        try {
            ((ImageView) findViewById(R.id.photoUserDetailImageView)).setImageBitmap(future.get());
        } catch (ExecutionException e) {
        } catch (InterruptedException e) {
        }
        ((TextView) findViewById(R.id.nameUserDetailTextView)).setText(user.getName());

        updateRecylerView();

        if (user.isFavorite()) {
            findViewById(R.id.favoriteUserDetailButton).setVisibility(View.GONE);
            findViewById(R.id.unfavoriteUserDetailButton).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.favoriteUserDetailButton).setVisibility(View.VISIBLE);
            findViewById(R.id.unfavoriteUserDetailButton).setVisibility(View.GONE);
        }
    }
    private void updateRecylerView() {
        PriorityQueue<Course> coursesPriorityQueue = new PriorityQueue<>();
        List<Course> courses = new ArrayList<>();
        for (Course userCourse : db.courseDao().getForUser(1)) {
            for (Course course : db.courseDao().getForUser(user.getId())) {
                if (userCourse.equals(course)) {
                    coursesPriorityQueue.add(course);
                }
            }
        }

        while (!coursesPriorityQueue.isEmpty()) {
            courses.add(coursesPriorityQueue.poll());
        }

        Log.d(TAG, "List of Common Courses for " + user.getName() + " and " + db.userDao().get(1).getName());
        for (Course commonCourse : courses) {
            String courseString = commonCourse.getYear() + commonCourse.getQuarter() + commonCourse.getSubject() + commonCourse.getNumber();
            Log.d(TAG, courseString);
        }
      
        coursesRecyclerView = findViewById(R.id.coursesUserDetailRecyclerView);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);
        coursesViewAdapter = new CoursesViewAdapter(courses);
        coursesRecyclerView.setAdapter(coursesViewAdapter);

    }

    public void onGoBackUserDetailButtonClicked(View view) {
        finish();
    }


    public void onFavoriteUserDetailButtonClicked(View view) {
        db.userDao().get(user.getId()).setFavorite(true);
        findViewById(R.id.favoriteUserDetailButton).setVisibility(View.GONE);
        findViewById(R.id.unfavoriteUserDetailButton).setVisibility(View.VISIBLE);
    }

    public void onUnfavoriteUserDetailButtonClicked(View view) {
        db.userDao().get(user.getId()).setFavorite(false);
        findViewById(R.id.favoriteUserDetailButton).setVisibility(View.VISIBLE);
        findViewById(R.id.unfavoriteUserDetailButton).setVisibility(View.GONE);
    }
}