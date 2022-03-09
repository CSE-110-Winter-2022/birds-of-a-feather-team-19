package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;

import java.util.Locale;

public class SharedClassSizePriorityAssigner implements UserPriorityAssigner{

    @Override
    public double getPriority(Course course) {
        /*switch (course.getSize().toLowerCase(Locale.ROOT)) {
            case "tiny":
                return 1.00;
            case "small":
                return 0.33;
            case "medium":
                return 0.18;
            case "large":
                return 0.10;
            case "huge":
                return 0.06;
            case "gigantic":
                return 0.03;
        }*/
        return 0;
    }
}
