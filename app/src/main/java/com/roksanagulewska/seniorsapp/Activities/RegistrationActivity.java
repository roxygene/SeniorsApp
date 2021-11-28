package com.roksanagulewska.seniorsapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.roksanagulewska.seniorsapp.R;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailEditTxt, passwordEditTxt, password2EditTxt;
    private Button registerBtn, goToLogInBtn;
    private FirebaseAuth auth;
    private int backCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailEditTxt = findViewById(R.id.emailEditTxt); //pole do wprowadzenia emaila
        passwordEditTxt = findViewById(R.id.passwordEditTxt); //pole do wprowadzenia hasła
        password2EditTxt = findViewById(R.id.password2EditTxt); //pole do ponownego wprowadzenia hasła
        registerBtn = findViewById(R.id.registerBtn); //przycisk rejestracji
        goToLogInBtn = findViewById(R.id.signInButton); //przycisk przekierowania do aktywności logowania

        auth = FirebaseAuth.getInstance(); //instancja autentykacji Firebase

        //Przycisk rejestracji
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConnected()) { //jeśli urządzenie ma połączenie z siecią
                    String email = emailEditTxt.getText().toString().trim(); //pobranie emaila z okna tekstowego
                    String password = passwordEditTxt.getText().toString().trim(); //pobranie hasła z pola tekstowego
                    String password2 = password2EditTxt.getText().toString().trim(); //pobranie drugi raz wprowadzonego hasła do pola tekstowego

                    if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)) { //jeżeli którekolwiek z pól tekstowych jest puste
                        Toast.makeText(getApplicationContext(), "Please add all required information.", Toast.LENGTH_SHORT).show();
                    }else if(password.length() < 8) { //jeżeli hasło ma mniej niż 8 znaków
                        Toast.makeText(getApplicationContext(), "Your password has to contain at least 8 characters.", Toast.LENGTH_SHORT).show();
                    }else if(!password2.equals(password)) { //jeżeli podane hasła nie są takie same
                        Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }else {
                        registerUser(email, password); //rejestracja użytkownika przy pomocy emaila i hasła
                    }
                } else { //jeśli urządzenie nie ma połączenia z siecią
                    //przejście do aktywności RegisterInternetAccessActivity
                    Intent i = new Intent(getApplicationContext(), RegisterInternetAccessActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });

        //sprawdzenie kliknięcia na przycisku signIn
        goToLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //w przypadku kliknięcia tego przycisku następuje przekierowanie do aktywności LogInActivity
                Intent logIntent = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(logIntent);
                finish();
            }
        });


    }

    private void registerUser(String email, String password) { //rejestracja użytkownika przy pomocy podanego przez niego emaila i hasła
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() { //utworzenie użytkownika przy pomocy emaila i hasła
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) { //jeżeli się udało
                    Toast.makeText(getApplicationContext(), "User registered succesfully!", Toast.LENGTH_SHORT).show();
                    Intent registerIntent = new Intent(getApplicationContext(), UsersInfoActivity.class); //utworzenie intencji
                    Bundle registerBundle = new Bundle(); //utworzenie bundle
                    registerBundle.putString("email", email); //włożenie do bundle adresu email
                    registerBundle.putString("password", password); //włożenie do bundle hasła
                    registerIntent.putExtras(registerBundle); //dodanie bundle'a do intencji
                    startActivity(registerIntent); //start aktywności przy pomocy intencji
                    finish();

                }else{ //jeżeli się nie udało
                    Toast.makeText(getApplicationContext(),"Registration failed!", Toast.LENGTH_SHORT).show();
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


