package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UserNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("name", ((TextView) findViewById(R.id.editTextUserName)).getText().toString());
        editor.apply();

        Intent intent = new Intent(this, UserPhotoActivity.class);
        startActivity(intent);
    }

    public void onConfirmClicked(View view) {
        if (((TextView) findViewById(R.id.editTextUserName)).getText().toString() == "") {
            Utilities.showAlert(this, "Please enter a name");
            return;
        }

        finish();
    }
}