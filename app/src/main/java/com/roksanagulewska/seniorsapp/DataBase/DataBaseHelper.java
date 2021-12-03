package com.roksanagulewska.seniorsapp.DataBase;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roksanagulewska.seniorsapp.Activities.ProfileInfoActivity;
import com.roksanagulewska.seniorsapp.Activities.StartingActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class DataBaseHelper {

    private FirebaseDatabase db = FirebaseDatabase.getInstance(); //instancja bazy danych
    private DatabaseReference databaseReference = db.getReference(); //referencja do bazy danych
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); //id aktualnie zalogowanego użytkownika
    private DatabaseReference currentUserReference = databaseReference.child("Users").child(getCurrentUserId()); //referencja do aktualnie zalogowanego użytkownika
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference(); //referencja do firebase storage

    public DataBaseHelper() { //pusty konstruktor
    }

    public Task<Void> addUserToDB(User user) { //stworzenie użytkownika w bazie danych z wartościami tokimi jak pola przyjętego w parametrze obiektu user
        return databaseReference.child("Users").child(currentUserId).setValue(user);
    }

    public void addPotentialMatchesToDb(List<User> potentialMatchesList) {//dodanie potencjalnych par do tabeli Connections aktualnie zalogowanego użytkownika
        for (User element : potentialMatchesList) {
            currentUserReference.child("Connections").child(element.getUserId()).child("Liked").setValue("not yet");
            currentUserReference.child("Connections").child(element.getUserId()).child("Matched").setValue("not yet");

        }
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public DatabaseReference getCurrentUserReference() {
        return currentUserReference;
    }

}
