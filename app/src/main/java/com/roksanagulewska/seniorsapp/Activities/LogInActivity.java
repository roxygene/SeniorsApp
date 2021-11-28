package com.roksanagulewska.seniorsapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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


public class LogInActivity extends AppCompatActivity {

    private EditText emailEditTxt, passwordEditTxt;
    private Button loginBtn, goToRegistrationBtn;
    FirebaseAuth auth;
    private int backCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailEditTxt = findViewById(R.id.emailEditTxt); //pole do wprowadzania emaila
        passwordEditTxt = findViewById(R.id.passwordEditTxt); //pole do wprowadzania hasła
        loginBtn = findViewById(R.id.loginBtn); //przycisk logowania
        goToRegistrationBtn = findViewById(R.id.signUpButton); //przycisk przekierowania do aktywności rejestracji

        auth = FirebaseAuth.getInstance(); //instancja autentykacji Firebase

        //przycisk logowania
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConnected()) { //jeśli urządzenie ma połączenie z siecią
                    String email = emailEditTxt.getText().toString().trim(); //pobranie emaila z okna tekstowego
                    String password = passwordEditTxt.getText().toString().trim(); //pobranie hasła z okna tekstowego
                    loginUser(email, password); //logowanie użytkownika przy pomocy emaila i hasła
                } else { //jeśli urządzenie nie ma połączenia z siecią
                    //przejście do aktywności LogInInternetAccessActivity
                    Intent i = new Intent(getApplicationContext(), LoginInternetAccessActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        goToRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //w przypadku kliknięcia tego przycisku następuje przekierowanie do aktywności RegistrationActivity
                Intent registerIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

    }

    private void loginUser(String email, String password) { //logowanie użytkownika przy pomocy podanego przez niego emaila i hasła
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() { //zalogowanie użytkownika
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) { //jeżeli się udało
                    Toast.makeText(getApplicationContext(), "Signing in succesfull!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(intent); //przejście do aktywności NavigationActivity
                    finish();
                }else{ //jeżeli się nie udało
                    Toast.makeText(getApplicationContext(),"Incorrect email or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //metoda sprawdzająca czy urządzenie jest połączone z internetem
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //sprawdzenie połącznia z siecią
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() { //przy dwukrotnym kliknięciu przycisku powrotu zamyka aplikację a przy jednokrotnym wyświetla komunikat
        backCounter++;

        if (backCounter == 2) {
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Press back again to exit.", Toast.LENGTH_SHORT).show();
        }
    }

}