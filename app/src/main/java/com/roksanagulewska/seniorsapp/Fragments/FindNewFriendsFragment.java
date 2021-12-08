package com.roksanagulewska.seniorsapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roksanagulewska.seniorsapp.Activities.StartingActivity;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.DataBase.User;
import com.roksanagulewska.seniorsapp.R;
import com.roksanagulewska.seniorsapp.SwipeCards.CardStackAdapter;
import com.roksanagulewska.seniorsapp.SwipeCards.CardStackCallback;
import com.roksanagulewska.seniorsapp.SwipeCards.ItemModel;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindNewFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindNewFriendsFragment extends Fragment {

    DataBaseHelper dbHelper = new DataBaseHelper();
    String minPrefAge;
    String maxPrefAge;
    String prefferedSex;
    List<User> potentialMatchesList = new ArrayList<>(); //lista id użytkowników pasujących do kryteriów zalogowanego użytkownika
    List<ItemModel> items = new ArrayList<>(); //lista obiektów czytanych przez adapter
    boolean isFirstTime = true; //flaga sprawdzająca czy addMatchesToDb zostało już raz wykonane


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = FindNewFriendsFragment.class.getSimpleName();
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;

    public FindNewFriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindNewFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindNewFriendsFragment newInstance(String param1, String param2) {
        FindNewFriendsFragment fragment = new FindNewFriendsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_find_new_friends, container, false);
        init(root);
        return root;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_find_new_friends, container, false);
    }

    private void init(View root) { //ta metoda CHYBA inicjalizuje cały ten widok kart, przygotowuje i tp
        CardStackView cardStackView = root.findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) { // ciągnięcie karty
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio); //w którą stronę i w jakiej proporcji
            }

            @Override
            public void onCardSwiped(Direction direction) { //przeciągnięcie karty
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);

                if (direction == Direction.Right){ //jeżeli przesunięta w prawo
                    Toast.makeText(getContext(), "Like", Toast.LENGTH_SHORT).show();

                    //zmiana statusu relacji użytkownika
                    String cardUserId = items.get(0).getUserId(); //pobranie id użytkownika z karty z wierzchu
                    Log.d("TEEEST", "user ID: " + cardUserId); //do wywalenia
                    Log.d("TEEEST", "user name: " + items.get(0).getName()); //do wywalenia
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("Liked", "yes");
                    dbHelper.getCurrentUserReference().child("Connections").child(cardUserId).updateChildren(map); //zmiana Liked not yet na yes
                } else if (direction == Direction.Left){ //jeżeli przesunięta w lewo
                    //Toast.makeText(getContext(), "Direction Left", Toast.LENGTH_SHORT).show();
                    String cardUser = items.get(0).getUserId(); //pobranie id użytkownika karty z wierzchu
                    Toast.makeText(getContext(), "Dislike", Toast.LENGTH_SHORT).show();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("Liked", "no");
                    dbHelper.getCurrentUserReference().child("Connections").child(cardUser).updateChildren(map); //zmiana not liked na no
                }

                items.remove(0);//kiedy karta została przesunięta usuń ją ze stosu

                // Paginacja
                if (manager.getTopPosition() == adapter.getItemCount() - 5){
                    paginate();
                }
            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", name: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", name: " + tv.getText());
            }
        });
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        adapter = new CardStackAdapter(items); //utworzenie adaptera przyjmującego obiekty z listy items
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter); //ustawienie adaptera
        cardStackView.setItemAnimator(new DefaultItemAnimator());

        checkUsersPreferences();
        listPotentialMatches();

    }

    private void paginate() { //paginacja
        List<ItemModel> oldCard = adapter.getItems();
        List<ItemModel> newCard = new ArrayList<>(items);
        CardStackCallback callback = new CardStackCallback(oldCard, newCard);
        DiffUtil.DiffResult results = DiffUtil.calculateDiff(callback);
        adapter.setItems(newCard);
        results.dispatchUpdatesTo(adapter);
    }

    public void checkUsersPreferences() { //sprawdza preferencje zalogowanego użytkownika
        ValueEventListener checkPreferencesValueEventListener = new ValueEventListener() { //utworzenie listenera
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //w przypadku dodania lub zmiany w bazie danych
                if(dataSnapshot.hasChildren()) {
                    prefferedSex = dataSnapshot.child("preferredSex").getValue().toString(); //przypisanie zmiennej preferowanej płci zalogowanego użytkownika
                    minPrefAge = dataSnapshot.child("minPrefAge").getValue().toString(); //przypisanie zmiennej preferowanego minimalnego wieku zalogowanego użytkownika
                    maxPrefAge = dataSnapshot.child("maxPrefAge").getValue().toString(); //przypisanie zmiennej preferowanego maksymalnego wieku zalogowanego użytkownika

                    //wypisania kontrolne, do wywalenia
                    Log.d("PREF", "SEX: " + prefferedSex);
                    Log.d("PREF", "MINage: " + minPrefAge);
                    Log.d("PREF", "MAXage: " + maxPrefAge);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("READ_PREFERENCES_ERROR", "Failed to read preferences values.", error.toException());
            }
        };

        dbHelper.getCurrentUserReference().addValueEventListener(checkPreferencesValueEventListener); //dodanie wyżej utworzonego listenera na referencji do zalogowanego użytkownika

    }

    public void listPotentialMatches() { //dodawanie użytkowników spełniających wymagania aktualnie zalogowanego użytkownika do listy
        potentialMatchesList.clear();
        ValueEventListener listPotentialMatchesValueEventListener = new ValueEventListener() { //utworzenie listenera
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //dla każdego użytkownika w Users
                    User user = snapshot.getValue(User.class); //tworzy obiekt użytkownika którego wartości pól są danymi pobranymi z bazy

                    //wypisania kontrolne, do wywalenia
                    Log.d("PREF", "sexP " + prefferedSex);
                    Log.d("PREF", "sexU " + user.getSex());
                    Log.d("PREF", "ageU " + user.getAge());

                        if (prefferedSex.equals(user.getSex()) || prefferedSex.equals("both")) { //sprawdzenie dopasowania płci
                            if (Integer.parseInt(user.getAge()) <= Integer.parseInt(maxPrefAge) && Integer.parseInt(user.getAge()) >= Integer.parseInt(minPrefAge)) { //sprawdzenie dopasowania wieku
                                if(!dbHelper.getCurrentUserId().equals(user.getUserId())) {//sprawdzenie czy użytkownik nie będzie wyświetlał sam siebie
                                    if(dataSnapshot.child(dbHelper.getCurrentUserId()).child("Connections").hasChild(user.getUserId())) { //sprawdzenie czyistnieje już taka pozycja w tabeli connections, aby nie nadpisywać danych
                                        Log.d("OMG", "yes " + user.getUserId());
                                    } else {
                                        Log.d("OMG", "no " + user.getUserId());
                                        potentialMatchesList.add(user); //dodanie obiektu użytkownika spełniającego wymagania do listy potentialMatchesList
                                    }
                                }
                            }
                        }
                }

                //wypisanie kontrolne, do wywalenia
                for (User element : potentialMatchesList) {
                    Log.d("PREF_USER", element.getEmail());
                }

                if (isFirstTime) { //wykorzystanie flagi w celu sprawdzenia czy nie nadpiszemy listy
                    dbHelper.addPotentialMatchesToDb(potentialMatchesList);
                    addList();
                    isFirstTime = false;
                }
                adapter.notifyDataSetChanged();//aktualizacja danych na adapterze
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("READ_POTENTIAL_MATCHES_ERROR", "Failed to read values.", error.toException());
            }
        };

        dbHelper.getDatabaseReference().child("Users").addValueEventListener(listPotentialMatchesValueEventListener); //ustawienia listenera na Users

    }

    private void addList() { //metoda tworzy obiekt item dla każdego użytkownika z listy potentialMatchesList
        items.clear();
        Log.d("PREFL", "ListaMatches: " + potentialMatchesList.size()); //wyświetlanie kontrolne, do wywalenia

        for (User potentialMatch : potentialMatchesList) {
            items.add(new ItemModel(potentialMatch.getEmail(), potentialMatch.getUserId(), potentialMatch.getName(), potentialMatch.getAge(), potentialMatch.getLocalisation(), potentialMatch.getMainPictureName(), potentialMatch.getImageUri(), potentialMatch.getDescription()));
        }

        Log.d("PREFL", "ListaItem: " + items.size()); //wyświetlanie kontrolne do wywalenia

    }
}