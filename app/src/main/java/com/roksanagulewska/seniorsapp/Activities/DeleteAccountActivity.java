package com.roksanagulewska.seniorsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.roksanagulewska.seniorsapp.Fragments.SettingsFragment;
import com.roksanagulewska.seniorsapp.R;

public class DeleteAccountActivity extends AppCompatActivity {

    Button yesBtn, noBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        yesBtn = findViewById(R.id.yesButton);
        noBtn = findViewById(R.id.noButton);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(backIntent);
                finish();
            }
        });

    }
}