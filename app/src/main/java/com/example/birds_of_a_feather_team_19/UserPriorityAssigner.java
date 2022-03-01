package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;

public interface UserPriorityAssigner {
    double getPriority(Course course);
}
