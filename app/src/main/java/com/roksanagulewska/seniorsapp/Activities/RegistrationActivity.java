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
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.R;
import com.roksanagulewska.seniorsapp.UserDB;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailEditTxt, passwordEditTxt, password2EditTxt;
    private Button registerBtn;

    UserDB user = new UserDB();
    DataBaseHelper db = new DataBaseHelper();
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailEditTxt = findViewById(R.id.emailEditTxt);
        passwordEditTxt = findViewById(R.id.passwordEditTxt);
        password2EditTxt = findViewById(R.id.password2EditTxt);
        registerBtn = findViewById(R.id.registerBtn);

        auth = FirebaseAuth.getInstance();

        //Zapisywanie danych w firebase przy kliknięciu przycisku
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditTxt.getText().toString().trim();
                String password = passwordEditTxt.getText().toString().trim();
                String password2 = password2EditTxt.getText().toString().trim();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)) {
                    Toast.makeText(getApplicationContext(), "Please add all required informations.", Toast.LENGTH_SHORT).show();
                }else if(password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Your password has to contain at least 8 characters.", Toast.LENGTH_SHORT).show();
                }else if(!password2.equals(password)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }else {
                    registerUser(email, password);
                    user.setEmail(email);
                    user.setPassword(password);
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

                    String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    db.addUserToDB(uId, email, password); //dodanie usera do bazy danych

                }else{
                    Toast.makeText(getApplicationContext(),"Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}