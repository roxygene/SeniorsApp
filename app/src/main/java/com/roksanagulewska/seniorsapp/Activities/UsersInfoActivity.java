package com.roksanagulewska.seniorsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.roksanagulewska.seniorsapp.R;

public class UsersInfoActivity extends AppCompatActivity {

    Button confirmInfoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_info);

        confirmInfoBtn = findViewById(R.id.confirmInfoButton);
    }

    public void goToPrefOnClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
        startActivity(intent);
        finish();
    }
}