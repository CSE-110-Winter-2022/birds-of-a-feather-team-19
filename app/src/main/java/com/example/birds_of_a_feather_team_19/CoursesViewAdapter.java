package com.example.birds_of_a_feather_team_19;

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

    public CoursesViewAdapter(List<Course> courses) {
        super();
        this.courses = courses;
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
        return courses.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            //implements View.OnClickListener
            {
        private final TextView courseNameView;
        private Course course;

        ViewHolder(View itemView) {
            super(itemView);
            this.courseNameView = itemView.findViewById(R.id.courses_row_text);
        }

        public void setCourse(Course course) {
            this.course = course;
            this.courseNameView.setText(course.getTitle());
        }

//        @Override
//        public void onClick(View view) {
//            Context context = view.getContext();
//            Intent intent = new Intent(context, UserDetailActivity.class);
//            intent.putExtra("user_id", this.user.getId());
//            context.startActivity(intent);
//        }
    }
}
