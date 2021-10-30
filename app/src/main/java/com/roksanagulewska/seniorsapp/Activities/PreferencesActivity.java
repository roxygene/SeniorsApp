package com.roksanagulewska.seniorsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.roksanagulewska.seniorsapp.R;

public class PreferencesActivity extends AppCompatActivity {

    Button confirmPrefBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        confirmPrefBtn = findViewById(R.id.confirmPrefButton);
    }

    public void goToUsersProfileOnClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
        startActivity(intent);
        finish();
    }
}