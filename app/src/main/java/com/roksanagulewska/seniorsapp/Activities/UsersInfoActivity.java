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
import com.roksanagulewska.seniorsapp.R;

public class UsersInfoActivity extends AppCompatActivity {

    EditText nameEditTxt;
    EditText localisationEditTxt;
    EditText ageEditTxt;
    SwitchCompat sexSwitch;
    Button confirmInfoBtn;
    String sex;
    int age = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_info);

        nameEditTxt = findViewById(R.id.nameEditText);
        localisationEditTxt = findViewById(R.id.localisationEditText);
        ageEditTxt = findViewById(R.id.ageEditText);
        sexSwitch = findViewById(R.id.sexSwitch);
        confirmInfoBtn = findViewById(R.id.confirmInfoButton);

        Intent registerIntent = getIntent(); //intencja która wywołała tą aktywność
        Bundle registerBundle = registerIntent.getExtras(); // przypisanie bundla który przyszedł z intencją
        String email = registerBundle.getString("email");
        String password = registerBundle.getString("password");

        //sprawdzanie stanu switcha
        sexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) { //Male
                    sex = "male";
                } else { //Female
                    sex = "female";
                }
            }
        });

        confirmInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEditTxt.getText().toString().trim();
                String localisation = localisationEditTxt.getText().toString().trim();
                age = Integer.parseInt(ageEditTxt.getText().toString().trim());

                //wywala wyjątek jak localisation albo name puste
                name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase(); //formatowanie imienia tak aby zaczynało się od wielkiej litery
                localisation = localisation.substring(0,1).toUpperCase() + localisation.substring(1).toLowerCase(); //formatowanie miasta tak aby zaczynało się od wielkiej litery

                if(name.isEmpty() || localisation.isEmpty() || age == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please add all required information.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent infoIntent = new Intent(getApplicationContext(), PreferencesActivity.class);
                    Bundle infoBundle = new Bundle();
                    infoBundle.putString("email", email);
                    infoBundle.putString("password", password);
                    infoBundle.putString("name", name);
                    infoBundle.putString("localisation", localisation);
                    infoBundle.putInt("age", age);
                    infoBundle.putString("sex", sex);
                    infoIntent.putExtras(infoBundle);
                    startActivity(infoIntent);
                    finish();
                }
            }
        });
    }
}