package com.roksanagulewska.seniorsapp.DataBase;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    private List<String> returnedList = new ArrayList<>(); //get i set

    public DataBaseHelper() {
    }

    public Task<Void> addUserToDB(User user) {
        return databaseReference.child("Users").child(currentUserId).setValue(user);
    }


    public void addAllUsersIdToList() {
        Log.d("LISTENER", "Przed add valueEventListener");

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() { //do poprawy
            /**
             * This method will be called with a snapshot of the data at this location. It will also be called
             * each time that data changes.
             *
             * @param dataSnapshot The current data at the location
             * */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                returnedList.clear();
                Log.d("LISTENER", "W onDataChange");
                Log.d("LISTENER", "Wchodzi do onDataChange");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    String id = user.getUserId();
                    returnedList.add(id);
                    Log.d("ID_USERA", id);
                    Log.d("LISTENER", "w  małej pętli");
                }
                //returnedList = list;
                Log.d("LISTENER", "Wyszedł z małej pętli");
                //Log.d("LISTENER", "Rozmiar list " + list.size());
                Log.d("LISTENER", "Rozmiar returnedList: " + returnedList.size());
                //StartingActivity.metodaX(returnedList);
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
                Log.w("READ_ERROR", "Failed to read value.", error.toException());
            }
        });

    }

    /*
    public ArrayList<String> addAllUsersIdToList1() {

    }

     */

    /*
            Log.d("LISTENER", "przed końcem dużej pętli przed return");
            if (returnedList.size() > 1) {
                Log.d("LISTENER", "return");
                return returnedList;
            }

             */

    /*
    public void generateAllUsersIdList(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String id : list)
        {
            sb.append(id + " ");
        }
        String idList = sb.toString();
        Log.d("ID_LISTA", idList);
    }

     */



    /*
    //wydobywa wszystkie klucze z Users
    public Query get() {
        return databaseReference.child("Users").orderByKey();
    }

     */
    //wydobywa wszystkie klucze z Users
    /*
    public Query get() {
        return databaseReference.child("Users").orderByChild();
    }

    public String loadData() {
        //ArrayList<User> usersId = new ArrayList<>();
        //ArrayList<User> users = new ArrayList<>();
        //ArrayList<String> usersIds = new ArrayList<>();
        final String[] usersIds = new String[1];
        get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersIds[0] = snapshot.getValue(String.class);
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    users.add(user);
                }

                for (User user : users) {
                    String id = user.getUserId();
                    usersIds.add(id);
                }


            //}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return usersIds[0];
    }
    */
/*
    public void addUsersInfo(String name, String localisation, int age, String sex, String prefferedSex, int minAge, int maxAge) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("localisation", localisation);
        map.put("age", age);
        map.put("sex", sex);
        map.put("prefferedSex", prefferedSex);
        map.put("minPrefAge", minAge);
        map.put("maxPrefAge", maxAge);
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).updateChildren(map);
    }


 */
/*
    public List<User> choosePrefferedUsers(String uId) {


    }

    public List<String> choosePrefferedUsersText(String uId) {


    }

 */
/*
    //przykładowa metoda do zmiany danych w bazie:
    public Task<Void> update(String userId, HashMap<String , Object> hashMap) {
        return databaseReference.child(userId).child("User").updateChildren(hashMap);
    }

 */
    /*
    //przykładowa metoda do usuwania konta z bazy danych
    public Task<Void> remove(String userId) {
        return databaseReference.child("User").child(userId).removeValue();
    }
     */

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
