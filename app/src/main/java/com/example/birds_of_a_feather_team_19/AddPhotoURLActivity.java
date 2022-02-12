package com.example.birds_of_a_feather_team_19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        String photoURL = ((TextView) findViewById(R.id.photoAddPhotoURLEditText)).getText().toString();
        if (photoURLInvalid(photoURL)) {
            Utilities.showAlert(this, "Please enter a valid URL");
            return;
        }

        SharedPreferences.Editor editor = getSharedPreferences("Birds of a Feather", MODE_PRIVATE).edit();
        editor.putString("photoURL", photoURL);
        editor.apply();

        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
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