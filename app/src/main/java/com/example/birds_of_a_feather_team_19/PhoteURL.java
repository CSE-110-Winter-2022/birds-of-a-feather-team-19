package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PhoteURL extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phote_url);

    }

    public void onSubmitURL(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, AddClasses.class);
        context.startActivity(intent);
    }


}