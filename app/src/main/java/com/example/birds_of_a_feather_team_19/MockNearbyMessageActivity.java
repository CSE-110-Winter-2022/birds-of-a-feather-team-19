package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Index;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.Course;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MockNearbyMessageActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_nearby_message);

        db = AppDatabase.singleton(this);
    }

    public void onBackClicked(View view) {
        finish();
    }

    public void onEnterMockNearbyMessageButtonClicked(View view) {
        TextView mockUserTextView = findViewById(R.id.userDetailMockNearbyMessageEditText);
        String userDetail = mockUserTextView.getText().toString();

        MockNearbyMessageListener.addMessage(userDetail);

        mockUserTextView.setText("");
    }

    private boolean photoURLInvalid(String photoURL) {
        ExecutorService imageExecutor = Executors.newSingleThreadExecutor();

        if (!photoURL.equals("")) {
            Future<Boolean> future = (imageExecutor.submit(() -> BitmapFactory.decodeStream(new URL(photoURL).openStream()) == null));
            try {
                return future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}