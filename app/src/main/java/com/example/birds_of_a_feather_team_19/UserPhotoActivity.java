package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UserPhotoActivity extends AppCompatActivity {
    public String photoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_phote);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("photo", ((TextView) findViewById(R.id.editTextUserPhoto)).getText().toString());
        editor.apply();

        Intent intent = new Intent(this, AddClassActivity.class);
        startActivity(intent);
    }

    public void onSubmitClicked(View view) {
        finish();
    }
}