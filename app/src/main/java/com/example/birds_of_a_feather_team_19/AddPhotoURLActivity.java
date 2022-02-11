package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.net.URL;

public class AddPhotoURLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_url);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public void onSubmitAddPhotoURLClicked(View view) {
        if (photoURLInvalid(((TextView) findViewById(R.id.editTextPhotoAddPhotoURL)).getText().toString())) {
            Utilities.showAlert(this, "Please enter a valid photo");
            return;
        }

        SharedPreferences.Editor editor = getSharedPreferences("Birds of a Feather", MODE_PRIVATE).edit();
        editor.putString("photoURL", ((TextView) findViewById(R.id.editTextPhotoAddPhotoURL)).getText().toString());
        editor.apply();

        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }
    private boolean photoURLInvalid(String photoURL) {
        if (!photoURL.equals("")) {
            try {
                new URL(photoURL).toURI();
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }
}