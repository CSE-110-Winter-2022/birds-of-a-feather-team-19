package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.birds_of_a_feather_team_19.model.db.AppDatabase;
import com.example.birds_of_a_feather_team_19.model.db.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected RecyclerView usersRecyclerView;
    protected RecyclerView.LayoutManager usersLayoutManager;
    protected UsersViewAdapter usersViewAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Birds of a Feather");

        db = AppDatabase.singleton(this);
        List<User> users = new ArrayList<>();// db.usersDao().getAll();

        usersRecyclerView = findViewById(R.id.recyclerViewUsers);
        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        usersViewAdapter = new UsersViewAdapter(users);
        usersRecyclerView.setAdapter(usersViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //checkBluetoothStatus();

        if (db.usersDao().get(1) == null) {
            Intent intent = new Intent(this, AddNameActivity.class);
            startActivity(intent);
        }
    }

    /*private void checkBluetoothStatus() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_DENIED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH)) {
                Utilities.showAlert(this, "This app requests permission to Bluetooth to connect you to other users. ");
            }
        }
    }

    */public void onStartStopClicked(View view) {
        /*Button button = findViewById(R.id.buttonStartStop);
        Intent intent = new Intent(MainActivity.this, BluetoothService.class);
        if (button.getText().toString().equals("Start")) {
            button.setText("Stop");
            loadUsers();
        }
        else {
            button.setText("Start");
            stopService(intent);
        }*/
    }/*

    private void loadUsers() {
    }*/
}
