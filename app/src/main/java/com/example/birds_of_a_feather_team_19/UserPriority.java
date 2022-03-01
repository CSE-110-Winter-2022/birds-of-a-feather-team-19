package com.example.birds_of_a_feather_team_19;

import com.example.birds_of_a_feather_team_19.model.db.User;

// Test correct ordering
public class UserPriority implements Comparable<UserPriority> {
    private User user;
    private double priority;

    public UserPriority(User user, double priority) {
        this.user = user;
        this.priority = priority;
    }

    public User getUser() {
        return user;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(UserPriority userPriority) {
        return  (userPriority.getPriority() > priority) ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        return user.equals(((UserPriority) o).getUser());
    }
}

