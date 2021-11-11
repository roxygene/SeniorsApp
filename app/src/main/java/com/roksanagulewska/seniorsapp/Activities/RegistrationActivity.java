package com.roksanagulewska.seniorsapp.Activities;

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
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.R;
import com.roksanagulewska.seniorsapp.UserDB;



public class RegistrationActivity extends AppCompatActivity {

    private EditText emailEditTxt, passwordEditTxt, password2EditTxt;
    private Button registerBtn, tryAgainBtn;
    private ConstraintLayout registerLayout, noInternetLayout;
    //boolean isConnected = false;
    ConnectivityManager connectivityManager;
   // Context context;

    //BroadcastReceiver broadcastReceiver;

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
        registerBtn = findViewById(R.id.registerBtn);;

        //noInternetLayout = findViewById(R.id.no_internet_layout);
        //registerLayout = findViewById(R.id.registration_layout);
        //tryAgainBtn = findViewById(R.id.tryAgainButton);

        auth = FirebaseAuth.getInstance();

        /*

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No internet access", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Net działa", Toast.LENGTH_SHORT).show();
        }

         */

        /*
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
            }
        });
*/
        //Zapisywanie danych w firebase przy kliknięciu przycisku
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConnected()) {
                    String email = emailEditTxt.getText().toString().trim();
                    String password = passwordEditTxt.getText().toString().trim();
                    String password2 = password2EditTxt.getText().toString().trim();

                    if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)) {
                        Toast.makeText(getApplicationContext(), "Please add all required information.", Toast.LENGTH_SHORT).show();
                    }else if(password.length() < 8) {
                        Toast.makeText(getApplicationContext(), "Your password has to contain at least 8 characters.", Toast.LENGTH_SHORT).show();
                    }else if(!password2.equals(password)) {
                        Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }else {
                        registerUser(email, password);
                        user.setEmail(email);
                        user.setPassword(password);
                    }
                } else {
                    Intent i = new Intent(getApplicationContext(), RegisterInternetAccessActivity.class);
                    startActivity(i);
                    finish();
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
/*
    private void registerNetworkCallback() {
        try {
            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    isConnected = true;
                }

                @Override
                public void onLost(@NonNull Network network) {
                    isConnected = false;
                }
            });
        } catch (Exception e) {
            isConnected = false;
        }
    }

    private void unregisterNetworkCallback(){
        connectivityManager.unregisterNetworkCallback(new ConnectivityManager.NetworkCallback());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkCallback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterNetworkCallback();
    }


 */
    /*
    private void checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =  connectivityManager.getActiveNetworkInfo();

        if(info != null) {
            if (info.isConnected()) {
                registerLayout.setVisibility(View.VISIBLE);
                noInternetLayout.setVisibility(View.GONE);
            } else {
                registerLayout.setVisibility(View.GONE);
                noInternetLayout.setVisibility(View.VISIBLE);
            }
        } else {
            registerLayout.setVisibility(View.GONE);
            noInternetLayout.setVisibility(View.VISIBLE);
        }
    }

*/

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

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



}


