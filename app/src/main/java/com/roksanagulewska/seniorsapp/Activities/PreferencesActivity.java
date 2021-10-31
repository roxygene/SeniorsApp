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
import com.roksanagulewska.seniorsapp.R;
import com.roksanagulewska.seniorsapp.UserDB;

public class PreferencesActivity extends AppCompatActivity {

    CheckBox femalesCB, malesCB;
    EditText minAgeEditTxt, maxAgeEditTxt;
    Button confirmPrefBtn;

    UserDB user = new UserDB();
    DataBaseHelper db = new DataBaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        femalesCB = findViewById(R.id.femalesCheckBox);
        malesCB = findViewById(R.id.malesCheckBox);
        minAgeEditTxt = findViewById(R.id.minimalAgeEditText);
        maxAgeEditTxt = findViewById(R.id.maximalAgeEditText);
        confirmPrefBtn = findViewById(R.id.confirmPrefButton);


        confirmPrefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minAge = Integer.parseInt(minAgeEditTxt.getText().toString().trim());
                int maxAge = Integer.parseInt(maxAgeEditTxt.getText().toString().trim());

                if ((!femalesCB.isChecked() && !malesCB.isChecked()) || minAge == 0 || maxAge == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please add all required informations.", Toast.LENGTH_SHORT).show();
                } else {
                    user.setMinPrefAge(minAge);
                    user.setMaxPrefAge(maxAge);

                    if (femalesCB.isChecked() && malesCB.isChecked()) {
                        user.setPreferredSex("both");
                    }else if (femalesCB.isChecked() && !malesCB.isChecked()) {
                        user.setPreferredSex("females");
                    } else {
                        user.setPreferredSex("males");
                    }

                    String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    db.addUsersPreferences(uId, user.getPreferredSex(), minAge, maxAge);

                    Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}