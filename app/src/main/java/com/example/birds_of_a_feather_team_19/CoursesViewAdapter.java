package com.example.birds_of_a_feather_team_19;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.birds_of_a_feather_team_19.model.db.Course;

import java.util.List;

public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.ViewHolder> {
    private final List<Course> courses;
    private static final String TAG = "BoF";

    public CoursesViewAdapter(List<Course> courses) {
        super();
        this.courses = courses;

        for (Course addedCourse : courses) {
            String courseString = addedCourse.getYear() + addedCourse.getQuarter() + addedCourse.getSubject() + addedCourse.getNumber();
            Log.d(TAG, courseString);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.course_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setCourse(courses.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Size of Courses" + courses.size());
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView courseNameView;

        ViewHolder(View itemView) {
            super(itemView);
            this.courseNameView = itemView.findViewById(R.id.titleCourseRowTextView);
        }

        public void setCourse(Course course) {
            this.courseNameView.setText(course.getYear() + " " + course.getQuarter() + " " +
                    course.getSubject() + " " + course.getNumber());
        }

    }
}
