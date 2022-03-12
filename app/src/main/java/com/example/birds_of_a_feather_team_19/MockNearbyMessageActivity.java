package com.example.birds_of_a_feather_team_19;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MockNearbyMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_nearby_message);
    }

    public void onBackClicked(View view) {
        finish();
    }

    public void onEnterMockNearbyMessageButtonClicked(View view) {
        TextView mockUserTextView = findViewById(R.id.userDetailMockNearbyMessageEditText);

        MockNearbyMessageListener.addMessage(mockUserTextView.getText().toString());
        mockUserTextView.setText("");

        Toast.makeText(this, R.string.toast_add_mock_user, Toast.LENGTH_SHORT).show();
    }

}