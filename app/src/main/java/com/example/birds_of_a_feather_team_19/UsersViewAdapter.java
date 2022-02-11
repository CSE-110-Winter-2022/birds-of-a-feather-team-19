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
import kotlin.jvm.internal.Ref.ObjectRef;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsersViewAdapter extends RecyclerView.Adapter<UsersViewAdapter.ViewHolder> {
    private final List<User> users;

    public UsersViewAdapter(List<User> users) {
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
        private final ImageView photo;
        private final TextView name;
        private User user;

        ViewHolder(View itemView) {
            super(itemView);
            this.photo = itemView.findViewById(R.id.imageViewPhotoUserRow);
            this.name = itemView.findViewById(R.id.textViewNameUserRow);
            itemView.setOnClickListener(this);
        }

        public void setUser(User user) {
            this.user = user;
            this.name.setText(user.getName());

            ExecutorService executor = Executors.newSingleThreadExecutor();
            final Handler handler = new Handler(Looper.getMainLooper());
            final ObjectRef image = new ObjectRef();
            image.element = (Bitmap) null;
            executor.execute((Runnable) (new Runnable() {
                public final void run() {
                    String photoURL = user.getPhotoURL();

                    try {
                        InputStream in = (new URL(photoURL)).openStream();
                        image.element = BitmapFactory.decodeStream(in);
                        handler.post((Runnable)(new Runnable() {
                            public final void run() {
                                photo.setImageBitmap((Bitmap) image.element);
                            }
                        }));
                    } catch (Exception var3) {
                        var3.printStackTrace();
                    }
                }
            }));
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
