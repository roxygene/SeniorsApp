package com.roksanagulewska.seniorsapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.DataBase.User;
import com.roksanagulewska.seniorsapp.R;
import com.roksanagulewska.seniorsapp.Matches.ItemMatchModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView listView;


    DataBaseHelper dbHelper = new DataBaseHelper();
    List<String> potentialMatchesList = new ArrayList<>(); //lista użytkowników z tabeli Connections zalogowanego użytkownika
    List<String> matchesList = new ArrayList<>(); //lista użytkowników z którymi zalogowany użytkownik został sparowany, z tego robimy tabelę matches
    List<User> friendsList = new ArrayList<>(); //z tabeli matches wyciągamy wszystkie id i dodajemy do tej listy
    List<ItemMatchModel> friendsListToDisplay = new ArrayList<>(); //lista tych item modeli do wyświetlania
    User user;

    ArrayAdapter adapter;

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

        listLikedUsers();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        adapter = new ArrayAdapter<ItemMatchModel>(getActivity(), android.R.layout.simple_list_item_1, friendsListToDisplay);

        listView = rootView.findViewById(R.id.friends_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return rootView;
    }

    public void listLikedUsers() {
        potentialMatchesList.clear();
        ValueEventListener checkLikesValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //dla każdego id usera z tabeli Connections
                    String potentialFriend = snapshot.getKey(); //pobieram to id i zapisuję w zmiennej
                    if(snapshot.child("Liked").getValue().toString().equals("yes")) { //sprawdzam czy zalogowany użytkownik polajkował tego usera
                        Log.d("MATCHX", "L: yes");
                        potentialMatchesList.add(potentialFriend);//jeśli tak to dodaję jego id do potentialMatchesList
                    } else if (snapshot.child("Liked").getValue().toString().equals("no")) { //jeśli nie to nie robię nic
                        Log.d("MATCHX", "L: no");
                    }
                }

                //testowe wyświetlenie listy polajkowanych osób
                for (String element : potentialMatchesList) {
                    Log.d("MATCHX", "PotMatList: " + element);
                }

                listMatches(potentialMatchesList); //wywołuję metodę która wypełnia listę matchy i zmienia status Matched u zalogowanego użytkownika
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("READ_LIKES_ERROR", "Failed to read preferences values.", error.toException());
            }
        };
        dbHelper.getCurrentUserReference().child("Connections").addValueEventListener(checkLikesValueEventListener); //ustawienie listenera
    }
     private void listMatches(List<String> list) {
         ValueEventListener checkIfMatchValueEventListener = new ValueEventListener() { //utworzenie listenera
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //dla każdego usera
                     if(potentialMatchesList.contains(snapshot.getKey())) { //sprawdzenie czy user jest na liście potentialMatchesList i jeśli tak
                         if (snapshot.child("Connections").child(dbHelper.getCurrentUserId()).exists()) { //za
                             if (snapshot.child("Connections").child(dbHelper.getCurrentUserId()).child("Liked").getValue().toString().equals("yes")) {//jeżeli ten user polajkował zalogowanego usera, to
                                 matchesList.add(snapshot.getKey()); //dodajemy go do listy matchy
                                 Map<String, Object> map = new HashMap<String, Object>();
                                 map.put("Matched", "yes");
                                 dbHelper.getCurrentUserReference().child("Connections").child(snapshot.getKey()).updateChildren(map); //zmiana Liked not yet na yes
                             } else if (snapshot.child("Connections").child(dbHelper.getCurrentUserId()).child("Liked").getValue().toString().equals("no")) {
                                 Map<String, Object> map = new HashMap<String, Object>();
                                 map.put("Matched", "no");
                                 dbHelper.getCurrentUserReference().child("Connections").child(snapshot.getKey()).updateChildren(map); //zmiana Liked not yet na no
                             }
                         }
                     }
                 }

                 //zamiana łańcucha z id usera na obiekt klasy User
                 for (String match : matchesList) {
                     Log.d("MATCHX", "matchesList: " + match);

                     if (dataSnapshot.hasChild(match)) {
                         User matchedUser = dataSnapshot.child(match).getValue(User.class);
                         Log.d("MATCHX", matchedUser.getName());
                         friendsList.add(matchedUser);
                     }
                 }

                 addItemMatchModelList();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         };

         dbHelper.getDatabaseReference().child("Users").addValueEventListener(checkIfMatchValueEventListener);


     }

    //metoda przekształcająca userów w itemMatchModel
    private void addItemMatchModelList() {
        friendsListToDisplay.clear();
        Log.d("MATCHX", "Matches list: " + matchesList.size());
        Log.d("MATCHX", "Friends list: " + friendsList.size());

        for (User friend : friendsList) {
            friendsListToDisplay.add(new ItemMatchModel(friend.getName(), friend.getMainPictureName(), friend.getImageUri(), friend.getUserId()));
        }

        Log.d("MATCHX", "Friends list to display: " + friendsListToDisplay.size());
    }


    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
    }
}