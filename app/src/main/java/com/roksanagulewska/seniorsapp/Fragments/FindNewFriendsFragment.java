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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindNewFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindNewFriendsFragment extends Fragment {

    DataBaseHelper dbHelper = new DataBaseHelper();
    int minPrefAge;
    int maxPrefAge;
    String prefferedSex;
    List<User> potentialMatchesList = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //zadeklarować i inicjalizować pole klasy databaseHelper

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

        /*checkUsersPreferences();
        listPotentialMatches();*/



        /*
        userReference.addValueEventListener(new ValueEventListener() { //do poprawy
            /**
             * This method will be called with a snapshot of the data at this location. It will also be called
             * each time that data changes.
             *
             * @param dataSnapshot The current data at the location
             *
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
            StartingActivity.metodaX(returnedList);
        }

        /**
         * This method will be triggered in the event that this listener either failed at the server, or
         * is removed as a result of the security and Firebase Database rules. For more information on
         * securing your data, see: <a
         * href="https://firebase.google.com/docs/database/security/quickstart" target="_blank"> Security
         * Quickstart</a>
         *
         * @param error A description of the error that occurred

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w("READ_ERROR", "Failed to read value.", error.toException());
        }
    });
         */
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

    private void init(View root) {
        checkUsersPreferences();
        listPotentialMatches();

        CardStackView cardStackView = root.findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Right){
                    Toast.makeText(getContext(), "Direction Right", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Top){
                    Toast.makeText(getContext(), "Direction Top", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Left){
                    Toast.makeText(getContext(), "Direction Left", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Bottom){
                    Toast.makeText(getContext(), "Direction Bottom", Toast.LENGTH_SHORT).show();
                }

                // Paginating
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
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
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
        adapter = new CardStackAdapter(addList());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
    }

    private void paginate() {
        List<ItemModel> oldCard = adapter.getItems();
        List<ItemModel> newCard = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(oldCard, newCard);
        DiffUtil.DiffResult results = DiffUtil.calculateDiff(callback);
        adapter.setItems(newCard);
        results.dispatchUpdatesTo(adapter);
    }

    private List<ItemModel> addList() {
        List<ItemModel> items = new ArrayList<>();
        Log.d("PREF", "ListaItem: " + items.size());
        Log.d("PREF", "ListaMatches: " + potentialMatchesList.size());

        for (User potentialMatch : potentialMatchesList) {
            items.add(new ItemModel(R.drawable.sample1, potentialMatch.getName(), potentialMatch.getAge(), potentialMatch.getLocalisation(), "descriptionABC"));
        }
        return items;
    }

    public void checkUsersPreferences() {
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

    }

    public void listPotentialMatches() {
        ValueEventListener listPotentialMatchesValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    Log.d("PREF", "sexP" + prefferedSex);
                    Log.d("PREF", "sexUS " + user.getSex());
                    Log.d("PREF", "ageUS" + user.getAge());

                    if (prefferedSex.equals(user.getSex()) || prefferedSex.equals("both")) { //sprawdzenie dopasowania płci
                        if (user.getAge() <= maxPrefAge && user.getAge() >= minPrefAge) {
                            if(!dbHelper.getCurrentUserId().equals(user.getUserId())) {
                                potentialMatchesList.add(user);
                            }
                        }
                    }

                    Log.d("PREF", "jestem w pętli" + user.getEmail());
                }

                for (User element : potentialMatchesList) {
                    Log.d("PREF_USER", element.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("READ_POTENTIAL_MATCHES_ERROR", "Failed to read values.", error.toException());
            }
        };

        dbHelper.getDatabaseReference().child("Users").addValueEventListener(listPotentialMatchesValueEventListener);

    }
}