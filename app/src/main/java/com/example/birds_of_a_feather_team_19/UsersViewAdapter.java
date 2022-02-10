package com.example.birds_of_a_feather_team_19;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.birds_of_a_feather_team_19.model.db.User;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
        try {
            holder.setUser(users.get(position));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView userNameView;
        ImageView userImage = null;
        private User user;

        ViewHolder(View itemView) {
            super(itemView);
            this.userNameView = itemView.findViewById(R.id.user_row_name);
            this.userImage = itemView.findViewById(R.id.user_image);
            itemView.setOnClickListener(this);
        }

        public void setUser(User user) throws IOException {
            this.user = user;
            this.userNameView.setText(user.getName());
            new DownloadImageTask(userImage)
                    .execute(user.getPhotoURL());
        }


        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("user_id", this.user.getId());
            context.startActivity(intent);
        }
    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
