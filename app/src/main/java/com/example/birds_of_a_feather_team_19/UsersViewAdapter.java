package com.example.birds_of_a_feather_team_19;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UsersViewAdapter extends RecyclerView.Adapter<UsersViewAdapter.ViewHolder> {
    private final List<UserPriority> userPriorities;

    public UsersViewAdapter(List<UserPriority> usersPriorities) {
        super();
        this.userPriorities = usersPriorities;
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
        holder.setUser(userPriorities.get(position));
    }

    @Override
    public int getItemCount() {
        return userPriorities.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private User user;
        private final ImageView photo;
        private final TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photoUserRowImageView);
            name = itemView.findViewById(R.id.nameUserRowTextView);
            itemView.setOnClickListener(this);
        }

        public void setUser(UserPriority userPriority) {
            this.user = userPriority.getUser();
            name.setText(user.getName() + " (" + userPriority.getPriority() + ")");

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Bitmap> future = (executor.submit(() -> BitmapFactory.decodeStream(new URL(user.getPhotoURL()).openStream())));
            try {
                photo.setImageBitmap(future.get());
            } catch (ExecutionException e) {
            } catch (InterruptedException e) {
            }
        }
      
        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("id", this.user.getId());
            context.startActivity(intent);
        }
    }
}
