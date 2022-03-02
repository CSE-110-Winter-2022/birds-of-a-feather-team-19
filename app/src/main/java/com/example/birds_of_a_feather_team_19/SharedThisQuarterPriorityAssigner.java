package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.Course;

import java.time.Month;
import java.time.MonthDay;
import java.util.Calendar;

public class SharedThisQuarterPriorityAssigner implements UserPriorityAssigner {


    @Override
    public double getPriority(Course course) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        String year = String.valueOf(c.get(Calendar.YEAR));
        String quarter = "";
        MonthDay current = MonthDay.of(month, day);

        //dates are acording to https://blink.ucsd.edu/instructors/resources/academic/calendars/2021.html
        MonthDay fallIn = MonthDay.of(Month.SEPTEMBER, 20);
        MonthDay fallOut = MonthDay.of(Month.DECEMBER, 11);
        MonthDay winterIn = MonthDay.of(Month.JANUARY, 3);
        MonthDay winterOut = MonthDay.of(Month.MARCH, 19);
        MonthDay springIn = MonthDay.of(Month.MARCH, 23);
        MonthDay springOut = MonthDay.of(Month.JUNE, 10);
        MonthDay summer1In = MonthDay.of(Month.JUNE, 27);
        MonthDay summer1Out = MonthDay.of(Month.JULY, 30);
        MonthDay summer2In = MonthDay.of(Month.AUGUST, 1);
        MonthDay summer2Out = MonthDay.of(Month.SEPTEMBER, 3);

        //there is no clear dates for special summer session
        //dates are from summer session 2 to fall
        MonthDay spSummerIn = MonthDay.of(Month.SEPTEMBER, 4);
        MonthDay spSummerOut = MonthDay.of(Month.SEPTEMBER, 19);


        if (current.compareTo(fallIn) >= 0 && current.compareTo(fallOut) >= 0) quarter = "fall";
        if (current.compareTo(winterIn) >= 0 && current.compareTo(winterOut) >= 0) quarter = "winter";
        if (current.compareTo(springIn) >= 0 && current.compareTo(springOut) >= 0) quarter = "spring";
        if (current.compareTo(summer1In) >= 0 && current.compareTo(summer1Out) >= 0) quarter = "summer session 1";
        if (current.compareTo(summer2In) >= 0 && current.compareTo(summer2Out) >= 0) quarter = "summer session 2";
        if (current.compareTo(spSummerIn) >= 0 && current.compareTo(spSummerOut) >= 0) quarter = "special summer session";

        if (course.getQuarter() == quarter && course.getYear() == year) return 1;
        return 0;
    }
}
