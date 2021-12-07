package com.roksanagulewska.seniorsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.Fragments.SettingsFragment;
import com.roksanagulewska.seniorsapp.R;

public class DeleteAccountActivity extends AppCompatActivity {

    Button yesBtn, noBtn;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = db.getReference();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference currentUserReference = databaseReference.child("Users").child(currentUserId);
    String imageUri;
    StorageReference imageStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        yesBtn = findViewById(R.id.yesButton);
        noBtn = findViewById(R.id.noButton);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DELETEX", "Naciśnięto tak");
                deleteCurrentUserAccount();
            }
        });


        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DELETEX", "Naciśnięto nie");
                Intent backIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(backIntent);
                finish();
            }
        });

    }

    private void deleteCurrentUserAccount() {
        Log.d("DELETEX", "w deleteCurrUser...");
        currentUserReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageUri = snapshot.child("imageUri").getValue().toString();
                imageStorageReference = firebaseStorage.getReferenceFromUrl(imageUri);
                Log.d("DELETEX", "Jestem w onDataChange");

                currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("DELETEX", "User deleted.");
                            Intent intent = new Intent(getApplicationContext(), StartingActivity.class);
                            Log.d("DELETEX", "intencja");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                /*imageStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DELETEX", "Image deleted.");
                        currentUserReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("DELETEX", "User data deleted.");
                                currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("DELETEX", "User deleted.");
                                            Toast.makeText(getApplicationContext(), "User account deleted.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });*/
            }

            /**
             * This method will be triggered in the event that this listener either failed at the server, or
             * is removed as a result of the security and Firebase Database rules. For more information on
             * securing your data, see: <a
             * href="https://firebase.google.com/docs/database/security/quickstart" target="_blank"> Security
             * Quickstart</a>
             *
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
}