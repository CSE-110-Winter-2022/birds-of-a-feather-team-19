package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;

public class SaveSessionActivity extends AppCompatActivity {
    private AppDatabase db;
    private static final String TAG = "BoF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_session);
        db = AppDatabase.singleton(this);
    }

    public void onSaveButtonClicked(View view) {
        Intent intent = getIntent();
        int id = intent.getIntExtra("session_id", 1);
        Log.d(TAG, "Session name originally " + db.sessionDao().get(id).getSessionName());
        TextView nameView = (TextView) findViewById(R.id.sessionNameTextView);
        String sessionName = nameView.getText().toString();
        db.sessionDao().update(id, sessionName);
        Log.d(TAG, "Session changed to " + db.sessionDao().get(id).getSessionName());
        finish();
    }
}