package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;

public class SharedClassesPriorityAssigner implements UserPriorityAssigner{

    @Override
    public double getPriority(Course course) {
        return 1;
    }
}
