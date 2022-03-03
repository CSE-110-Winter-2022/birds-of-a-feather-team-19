package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;

import java.time.Month;
import java.time.MonthDay;
import java.util.Calendar;

public class SharedThisQuarterPriorityAssigner implements UserPriorityAssigner {
    @Override
    public double getPriority(Course course) {
        String[] quarterYear = Utilities.getCurrentQuarterYear();
        return (course.getQuarter().equals(quarterYear[0]) && course.getYear().equals(quarterYear[1])) ? 1 : 0;
    }
}
