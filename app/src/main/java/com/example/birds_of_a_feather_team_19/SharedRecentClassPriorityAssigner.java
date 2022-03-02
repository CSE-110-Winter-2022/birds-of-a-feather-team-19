package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;

public class SharedRecentClassPriorityAssigner implements UserPriorityAssigner {

    @Override
    public double getPriority(Course course) {
        int age = 0;
        switch (age) {
            case 0:
                return 5;
            case 1:
                return 4;
            case 2:
                return 3;
            case 3:
                return 2;
            case 4:
                return 1;
            case 5:
                return 0;
        }
        return 0;

    }

}

