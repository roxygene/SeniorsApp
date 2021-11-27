package com.roksanagulewska.seniorsapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.DataBase.User;
import com.roksanagulewska.seniorsapp.R;
import com.roksanagulewska.seniorsapp.SwipeCards.ItemMatchModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment {

    DataBaseHelper dbHelper = new DataBaseHelper();
    List<String> potentialMatchesList = new ArrayList<>();
    List<String> matchesList = new ArrayList<>();
    List<User> friendsList = new ArrayList<>();
    List<ItemMatchModel> friendsListToDisplay = new ArrayList<>();
    User user;
    boolean isAMatch = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        loadMatches();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }


    /*public void checkUsersPreferences() {
        ValueEventListener checkPreferencesValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prefferedSex = dataSnapshot.child("preferredSex").getValue().toString();
                minPrefAge = Integer.parseInt(dataSnapshot.child("minPrefAge").getValue().toString());
                maxPrefAge = Integer.parseInt(dataSnapshot.child("maxPrefAge").getValue().toString());

                Log.d("PREF", "SEX: " + prefferedSex);
                Log.d("PREF", "MINage: " + minPrefAge);
                Log.d("PREF", "MAXage: " + maxPrefAge);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("READ_PREFERENCES_ERROR", "Failed to read preferences values.", error.toException());
            }
        };

        dbHelper.getCurrentUserReference().addValueEventListener(checkPreferencesValueEventListener);

    }*/

    /*public void listPotentialMatches() {
        potentialMatchesList.clear();
        ValueEventListener listPotentialMatchesValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    Log.d("PREF", "sexP" + prefferedSex);
                    Log.d("PREF", "sexUS " + user.getSex());
                    Log.d("PREF", "ageUS" + user.getAge());

                        if (prefferedSex.equals(user.getSex()) || prefferedSex.equals("both")) { //sprawdzenie dopasowania płci
                            if (user.getAge() <= maxPrefAge && user.getAge() >= minPrefAge) { //sprawdzenie dopasowania wieku
                                if(!dbHelper.getCurrentUserId().equals(user.getUserId())) {//sprawdzenie czy użytkownik nie będzie wyświetlał sam siebie
                                    if(dataSnapshot.child(dbHelper.getCurrentUserId()).child("Connections").hasChild(user.getUserId())) { //sprawdzenie czyistnieje już taka pozycja w tabeli connections, aby nie nadpisywać danych
                                        Log.d("OMG", "yes " + user.getUserId());
                                    } else {
                                        Log.d("OMG", "no " + user.getUserId());
                                        potentialMatchesList.add(user);
                                    }
                                }
                            }
                        }

                    Log.d("PREF", "jestem w pętli" + user.getEmail());
                }

                for (User element : potentialMatchesList) {
                    Log.d("PREF_USER", element.getEmail());
                }

                if (isFirstTime) {
                    generateListOfMatches();
                    isFirstTime = false;
                }
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("READ_POTENTIAL_MATCHES_ERROR", "Failed to read values.", error.toException());
            }
        };

        dbHelper.getDatabaseReference().child("Users").addValueEventListener(listPotentialMatchesValueEventListener);

    }*/

    public void loadMatches() {
        matchesList.clear();
        potentialMatchesList.clear();
        ValueEventListener checkLikesValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String potentialFriend = dataSnapshot.getKey();
                    Log.d("MATCH", potentialFriend + " id frienda");
                    if(snapshot.child("Liked").getValue() == "yes") {
                        Log.d("MATCH", "L: yes");
                        potentialMatchesList.add(potentialFriend);
                    } else {
                        Log.d("MATCH", "L: no");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("READ_LIKES_ERROR", "Failed to read preferences values.", error.toException());
            }
        };

        ValueEventListener checkIfMatchValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Liked").getValue() == "yes") {
                    isAMatch = true;
                    //matchesList.add(element);
                }

                /*for(String element : potentialMatchesList) {
                    if(dataSnapshot.child(element).child("Connections").child(dbHelper.getCurrentUserId()).child("Liked").getValue() == "yes") {
                        dbHelper.getDatabaseReference().child("Users").child(dbHelper.getCurrentUserId()).child("Connections").child(element).child("Matched").setValue("yes");
                        matchesList.add(element);
                    }
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dbHelper.getCurrentUserReference().child("Connections").addValueEventListener(checkLikesValueEventListener);

        for(String element : potentialMatchesList) {
            dbHelper.getDatabaseReference().child("Users").child(element).child("Connections").child(dbHelper.getCurrentUserId()).addValueEventListener(checkIfMatchValueEventListener);
            if(isAMatch == true) {
                matchesList.add(element);
            }

            isAMatch = false;

        }

        for (String match : matchesList) {
            Log.d("MATCHX", match);
        }



    }


    //ta metoda ma tworzyć listę userów pasujących do tych z listy matchesList
    private void generateListOfFriends() {
        friendsList.clear();

        ValueEventListener isAMatchValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dbHelper.getDatabaseReference().child("Users").addValueEventListener(isAMatchValueEventListener);
    }

    //metoda przekształcająca userów w itemMatchModel


}