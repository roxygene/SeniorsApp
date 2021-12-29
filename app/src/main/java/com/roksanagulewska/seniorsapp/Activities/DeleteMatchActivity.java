package com.roksanagulewska.seniorsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.Fragments.MessagesFragment;
import com.roksanagulewska.seniorsapp.R;

import java.util.HashMap;
import java.util.Map;

public class DeleteMatchActivity extends AppCompatActivity {

    Button yesBtn, noBtn;
    DataBaseHelper dbHelper = new DataBaseHelper();
    String matchId;
    /*FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = db.getReference();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference currentUserReference = databaseReference.child("Users").child(currentUserId);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_match);

        yesBtn = findViewById(R.id.yesButton);
        noBtn = findViewById(R.id.noButton);

        Intent chatIntent = getIntent();
        Bundle chatBundle = chatIntent.getExtras();
        matchId = chatBundle.getString("matchId");

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DELETEX", "Naciśnięto tak");
                deleteMatch();
                Intent messageIntent = new Intent(getApplicationContext(), MessagesFragment.class);
                startActivity(messageIntent);
                finish();
            }
        });


        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DELETEX", "Naciśnięto nie");
                Intent messageIntent = new Intent(getApplicationContext(), MessagesFragment.class);
                startActivity(messageIntent);
                finish();
            }
        });
    }

    private void deleteMatch() {
        Log.d("DELETEX", "w deleteMatch...");
        Map<String, Object> map = new HashMap<>();
        map.put("Liked", "no");
        //map.put("Matched", "no");

        dbHelper.getCurrentUserReference().child("Connections").child(matchId).updateChildren(map);
        Toast.makeText(getApplicationContext(), "Contact deleted.", Toast.LENGTH_SHORT);
    }
}