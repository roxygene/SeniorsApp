package com.roksanagulewska.seniorsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.Fragments.MessagesFragment;
import com.roksanagulewska.seniorsapp.R;
import com.squareup.picasso.Picasso;

public class MatchProfileActivity extends AppCompatActivity {

    ImageView imageView;
    TextView nameTxt, localisationTxt, ageTxt, descriptionTxt;
    MaterialButton backBtn;
    DataBaseHelper dbHelper = new DataBaseHelper();
    String matchName;
    String matchLocalisation;
    String matchAge;
    String matchDescription;
    String matchImageName;
    String matchImageUri;
    String matchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile);

        Log.d("MYPROF", "my profileONCRV");

        Intent chatIntent = getIntent();
        Bundle chatBundle = chatIntent.getExtras();
        matchId = chatBundle.getString("matchId");

        imageView = findViewById(R.id.usersImage);
        nameTxt = findViewById(R.id.usersName);
        localisationTxt = findViewById(R.id.usersLocalisation);
        ageTxt = findViewById(R.id.usersAge);
        descriptionTxt = findViewById(R.id.usersDescription);
        backBtn = findViewById(R.id.backBtn);

        ValueEventListener matchDataValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()) {
                    matchName = snapshot.child("name").getValue().toString();
                    matchLocalisation = snapshot.child("localisation").getValue().toString();
                    matchAge = snapshot.child("age").getValue().toString();
                    matchDescription = snapshot.child("description").getValue().toString();
                    matchImageName = snapshot.child("mainPictureName").getValue().toString();
                    matchImageUri = snapshot.child("imageUri").getValue().toString();

                    displayUsersInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(getApplicationContext(), MessagesFragment.class);
                startActivity(messageIntent);
                finish();
            }
        });

        dbHelper.getDatabaseReference().child("Users").child(matchId).addValueEventListener(matchDataValueEventListener);
    }

    private void displayUsersInfo() {
        if (matchImageName.length() > 27) {
            Picasso.get()
                    .load(matchImageUri)
                    .fit()
                    .centerCrop()
                    .rotate(90) //jeżeli to zdjęce wykonane aparatem to obróć o 90 stopni
                    .into(imageView);

        } else {
            Picasso.get()
                    .load(matchImageUri)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }
        nameTxt.setText(matchName);
        localisationTxt.setText(matchLocalisation);
        ageTxt.setText(matchAge);
        descriptionTxt.setText(matchDescription);
    }
}