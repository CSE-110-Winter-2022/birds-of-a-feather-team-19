package com.example.birds_of_a_feather_team_19;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;

public class UserNameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] listOfAccounts = manager.getAccountsByType("com.google");
        String googleName;
        if(listOfAccounts.length > 0) {
            googleName = listOfAccounts[0].name;
        }
    }
}