package com.example.birds_of_a_feather_team_19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddPhotoURLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(this.getString(R.string.TAG), "Add photo URL activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_url);
    }

    @Override
    protected void onStop() {
        Log.d(this.getString(R.string.TAG), "Add photo URL activity finished");
        super.onStop();
        finish();
    }

    public void onSubmitAddPhotoURLButtonClicked(View view) {
        String photoURL = ((TextView) findViewById(R.id.photoURLAddPhotoURLEditText)).getText().toString();
        Log.d(this.getString(R.string.TAG), "Photo URL entered: " + photoURL);
        if (Utilities.invalidPhotoURL(photoURL)) {
            Utilities.showAlert(this, "Please enter a valid URL");
            return;
        }

        SharedPreferences.Editor editor = getSharedPreferences("Birds of a Feather", MODE_PRIVATE).edit();
        editor.putString("photoURL", photoURL);
        editor.apply();

        startActivity(new Intent(this, AddCourseActivity.class));
    }
}