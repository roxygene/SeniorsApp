package com.roksanagulewska.seniorsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.DataBase.User;
import com.roksanagulewska.seniorsapp.R;

public class PreferencesActivity extends AppCompatActivity {

    CheckBox femalesCB, malesCB;
    EditText minAgeEditTxt, maxAgeEditTxt;
    Button confirmPrefBtn;
    int minAge;
    int maxAge;
    String preferredSex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        femalesCB = findViewById(R.id.femalesCheckBox);
        malesCB = findViewById(R.id.malesCheckBox);
        minAgeEditTxt = findViewById(R.id.minimalAgeEditText);
        maxAgeEditTxt = findViewById(R.id.maximalAgeEditText);
        confirmPrefBtn = findViewById(R.id.confirmPrefButton);

        Intent infoIntent = getIntent(); //intencja która wywołała tą aktywność
        Bundle infoBundle = infoIntent.getExtras(); // przypisanie bundla który przyszedł z intencją
        String email = infoBundle.getString("email");
        String password = infoBundle.getString("password");
        String name = infoBundle.getString("name");
        String localisation = infoBundle.getString("localisation");
        int age = infoBundle.getInt("age");
        String sex = infoBundle.getString("sex");

        confirmPrefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    minAge = Integer.parseInt(minAgeEditTxt.getText().toString().trim());
                    maxAge = Integer.parseInt(maxAgeEditTxt.getText().toString().trim());
                } catch (NumberFormatException exception) { //jeżeli wpisany text nie będzie liczbą, zostanie wyrzucony wyjątek
                    if (age != 0) {
                        Toast.makeText(getApplicationContext(), "Incorrect age format.", Toast.LENGTH_LONG).show(); //komunikat informujący o wyrzuceniu wyjątku
                    }
                }

                if ((!femalesCB.isChecked() && !malesCB.isChecked()) || minAgeEditTxt.getText().toString().isEmpty() || maxAgeEditTxt.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please add all required information.", Toast.LENGTH_SHORT).show();
                } else {

                    if (minAge < 18 || maxAge < 18 || minAge > 130 || maxAge > 130 || minAge >= maxAge) {
                        Toast.makeText(getApplicationContext(), "Insert correct age, it must be greater or equal to 18.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (femalesCB.isChecked() && malesCB.isChecked()) { // sprawdzenie zaznaczenia checkboxów
                            preferredSex = "both";
                        }else if (femalesCB.isChecked() && !malesCB.isChecked()) {
                            preferredSex = "female";
                        } else {
                            preferredSex = "male";
                        }

                        Intent prefIntent = new Intent(getApplicationContext(), ProfileInfoActivity.class);
                        Bundle prefBundle =  new Bundle();
                        prefBundle.putString("email", email);
                        prefBundle.putString("password", password);
                        prefBundle.putString("name", name);
                        prefBundle.putString("localisation", localisation);
                        prefBundle.putInt("age", age);
                        prefBundle.putString("sex", sex);
                        prefBundle.putString("preferredSex", preferredSex);
                        prefBundle.putInt("minAge", minAge);
                        prefBundle.putInt("maxAge", maxAge);
                        prefIntent.putExtras(prefBundle);
                        startActivity(prefIntent);
                        finish();
                    }

                }

            }
        });
    }
}
