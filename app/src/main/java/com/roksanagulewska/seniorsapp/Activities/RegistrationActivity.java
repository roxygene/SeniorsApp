package com.roksanagulewska.seniorsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.roksanagulewska.seniorsapp.R;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailEditTxt, passwordEditTxt;
    private Button registerBtn;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailEditTxt = findViewById(R.id.emailEditTxt);
        passwordEditTxt = findViewById(R.id.passwordEditTxt);
        registerBtn = findViewById(R.id.registerBtn);

        auth = FirebaseAuth.getInstance();

        //Zapisywanie danych w firebase przy kliknięciu przycisku
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditTxt.getText().toString();
                String password = passwordEditTxt.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please add all required informations.", Toast.LENGTH_SHORT).show();
                }else if(password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Your password has to contain at least 8 characters.", Toast.LENGTH_SHORT).show();
                }else {
                    registerUser(email, password);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            //po utworzeniu użytkownika trzeba wyświetlić jakiś komunikat czy się udało
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User registered succesfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), UsersInfoActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}