package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PhotoURL extends AppCompatActivity {
    public String photoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phote_url);

    }

    public void setURL(){
        EditText url = (EditText)findViewById(R.id.photo_url);
        photoURL = url.getText().toString();
    }

    public void onSubmitURL(View view) {
        setURL();
        Intent intent = new Intent(this, AddClassActivity.class);
        intent.putExtra("photo_url", photoURL);
        startActivity(intent);
    }


}