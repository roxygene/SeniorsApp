package com.roksanagulewska.seniorsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.R;
import com.roksanagulewska.seniorsapp.UserDB;

public class UsersInfoActivity extends AppCompatActivity {

    EditText nameEditTxt;
    EditText localisationEditTxt;
    EditText ageEditTxt;
    SwitchCompat sexSwitch;
    Button confirmInfoBtn;

    UserDB user = new UserDB();
    DataBaseHelper db = new DataBaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_info);

        nameEditTxt = findViewById(R.id.nameEditText);
        localisationEditTxt = findViewById(R.id.localisationEditText);
        ageEditTxt = findViewById(R.id.ageEditText);
        sexSwitch = findViewById(R.id.sexSwitch);
        confirmInfoBtn = findViewById(R.id.confirmInfoButton);

        //sprawdzanie stanu switcha
        sexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) { //Male
                    user.setSex("male");
                } else { //Female
                    user.setSex("female");
                }
            }
        });

        confirmInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditTxt.getText().toString().trim();
                String localisation = localisationEditTxt.getText().toString().trim();
                int age = Integer.parseInt(ageEditTxt.getText().toString().trim());

                name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase(); //formatowanie imienia tak aby zaczynało się od wielkiej litery
                localisation = localisation.substring(0,1).toUpperCase() + localisation.substring(1).toLowerCase(); //formatowanie miasta tak aby zaczynało się od wielkiej litery

                if(name.isEmpty() || localisation.isEmpty() || age == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please add all required information.", Toast.LENGTH_SHORT).show();
                } else {
                    user.setName(name);
                    user.setLocalisation(localisation);
                    user.setAge(age);

                    String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    db.addUsersInfo(uId, name, localisation, age, user.getSex());

                    Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}