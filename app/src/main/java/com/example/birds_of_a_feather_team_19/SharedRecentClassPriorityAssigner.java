package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;

public class SharedRecentClassPriorityAssigner implements UserPriorityAssigner {

    @Override
    public double getPriority(Course course) {
        String[] currentQuarterYear = Utilities.getCurrentQuarterYear();
        int age = Utilities.getCourseAge(course.getYear(), course.getQuarter(), currentQuarterYear[1], currentQuarterYear[0]);
        return 5 - age;
    }

}

