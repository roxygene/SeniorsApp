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

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = db.getReference();
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference currentUserReference = databaseReference.child("Users").child(getCurrentUserId());


    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private List<String> returnedList = new ArrayList<>(); //get i set

    public DataBaseHelper() {
    }

    public Task<Void> addUserToDB(User user) {
        return databaseReference.child("Users").child(currentUserId).setValue(user);
    }

    public void addPotentialMatchesToDb(List<User> potentialMatchesList) {
        for (User element : potentialMatchesList) {
            //if (!(databaseReference.child("Users").child(currentUserId).child("Connections").child(element.getUserId())) {
                currentUserReference.child("Connections").child(element.getUserId()).child("Liked").setValue("not yet");
                currentUserReference.child("Connections").child(element.getUserId()).child("Matched").setValue("no yet");
            //}
        }
    }



    public StorageReference getStorageReference() {
        return storageReference;
    }

    public void setStorageReference(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    public FirebaseDatabase getDb() {
        return db;
    }

    public void setDb(FirebaseDatabase db) {
        this.db = db;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public DatabaseReference getCurrentUserReference() {
        return currentUserReference;
    }

    public void setCurrentUserReference(DatabaseReference currentUserReference) {
        this.currentUserReference = currentUserReference;
    }

    public List<String> getReturnedList() {
        return returnedList;
    }

    public void setReturnedList(List<String> returnedList) {
        this.returnedList = returnedList;
    }
}
