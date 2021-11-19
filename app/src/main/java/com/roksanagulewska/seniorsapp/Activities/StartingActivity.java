package com.roksanagulewska.seniorsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.Thread.sleep;

public class StartingActivity extends AppCompatActivity {

    private Button loginButton, registerButton;
    DataBaseHelper dbHelper = new DataBaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        loginButton = findViewById(R.id.loginBtn);
        registerButton = findViewById(R.id.registerBtn);

    }

    public void goToLogInOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToRegisterOnClick(View view) {
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

}