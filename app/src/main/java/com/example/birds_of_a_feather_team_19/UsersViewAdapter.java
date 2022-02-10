package com.example.birds_of_a_feather_team_19;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birds_of_a_feather_team_19.model.db.User;
import com.example.birds_of_a_feather_team_19.model.db.UserWithCourses;

import java.util.List;

public class UsersViewAdapter extends RecyclerView.Adapter<UsersViewAdapter.ViewHolder> {
    private final List<UserWithCourses> users;

    public UsersViewAdapter(List<UserWithCourses> users) {
        super();
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.user_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setUser(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView userNameView;
        private UserWithCourses user;

        ViewHolder(View itemView) {
            super(itemView);
            this.userNameView = itemView.findViewById(R.id.user_name);
            itemView.setOnClickListener(this);
        }

        public void setUser(UserWithCourses user) {
            this.user = user;
            this.userNameView.setText(user.getName());
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("user", this.user.getId());
            context.startActivity(intent);
        }
    }
}
