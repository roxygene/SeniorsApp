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
    int minAge = 0;
    int maxAge = 0;
    String preferredSex;

    User user;
    DataBaseHelper dbHelper = new DataBaseHelper();

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
                minAge = Integer.parseInt(minAgeEditTxt.getText().toString().trim());
                maxAge = Integer.parseInt(maxAgeEditTxt.getText().toString().trim());

                if ((!femalesCB.isChecked() && !malesCB.isChecked()) || minAge == 0 || maxAge == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please add all required information.", Toast.LENGTH_SHORT).show();
                } else {

                    if (femalesCB.isChecked() && malesCB.isChecked()) {
                        preferredSex = "both";
                    }else if (femalesCB.isChecked() && !malesCB.isChecked()) {
                        preferredSex = "females";
                    } else {
                        preferredSex = "males";
                    }

                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    user = new User(currentUserId, email, password, name, age, sex, localisation, preferredSex, null, null, minAge, maxAge);
                    dbHelper.addUserToDB(user).addOnSuccessListener(success->
                    {
                        Toast.makeText(getApplicationContext(), "User added to database!", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(error->
                    {
                        Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });

                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}