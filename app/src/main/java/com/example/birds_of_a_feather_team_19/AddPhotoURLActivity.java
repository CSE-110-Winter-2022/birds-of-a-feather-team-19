package com.example.birds_of_a_feather_team_19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class AddPhotoURLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_url);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void onSubmitAddPhotoURLButtonClicked(View view) {
        String photo = ((TextView) findViewById(R.id.photoAddPhotoURLEditText)).getText().toString();
        if (photoURLInvalid(photo)) {
            Utilities.showAlert(this, "Please enter a valid URL");
            return;
        }

        SharedPreferences.Editor editor = getSharedPreferences("Birds of a Feather", MODE_PRIVATE).edit();
        editor.putString("photo", photo);
        editor.apply();

        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }
    private boolean photoURLInvalid(String photo) {
        if (!photo.equals("")) {
            try {
                if (BitmapFactory.decodeStream((new URL(photo)).openStream()) == null) {
                    return true;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        }
        return false;
    }
}