package com.example.birds_of_a_feather_team_19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public void onConfirmAddNameButtonClicked(View view) {
        String name = ((EditText) findViewById(R.id.nameAddNameEditText)).getText().toString();
        if (Utilities.invalidName(name)) {
            Utilities.showAlert(this, "Please enter a name");
            return;
        }

        SharedPreferences.Editor editor = getSharedPreferences("Birds of a Feather", MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.apply();

        startActivity(new Intent(this, AddPhotoURLActivity.class));
    }
}