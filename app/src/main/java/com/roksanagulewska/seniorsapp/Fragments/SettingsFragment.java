package com.roksanagulewska.seniorsapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.roksanagulewska.seniorsapp.Activities.DeleteAccountActivity;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    Button confirmBtn, deleteAccountBtn;
    EditText nameTxt, localisationTxt, ageTxt, descriptionTxt, minAgeTxt, maxAgeTxt;
    CheckBox femalesCB, malesCB;

    DataBaseHelper dbHelper = new DataBaseHelper();

    String currentUserName;
    String currentUserLocalisation;
    String currentUserAge;
    String currentUserDescription;
    String currentUserPreferredSex;
    String currentUserMinPrefAge;
    String currentUserMaxPrefAge;
    int age, minAge, maxAge;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Log.d("SETTX", "jestem w oCV");

        nameTxt = rootView.findViewById(R.id.nameEditText);
        localisationTxt = rootView.findViewById(R.id.localisationEditText);
        ageTxt = rootView.findViewById(R.id.ageEditText);
        descriptionTxt = rootView.findViewById(R.id.descriptionEditText);
        femalesCB = rootView.findViewById(R.id.femalesCheckBox);
        malesCB = rootView.findViewById(R.id.malesCheckBox);
        minAgeTxt = rootView.findViewById(R.id.minAgeEditText);
        maxAgeTxt = rootView.findViewById(R.id.maxAgeEditText);
        confirmBtn = rootView.findViewById(R.id.confirmBtn);
        deleteAccountBtn = rootView.findViewById(R.id.deleteBtn);

        ValueEventListener currentUsersDataValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserName  = snapshot.child("name").getValue().toString();
                currentUserLocalisation = snapshot.child("localisation").getValue().toString();
                currentUserAge = snapshot.child("age").getValue().toString();
                currentUserDescription = snapshot.child("description").getValue().toString();
                currentUserPreferredSex = snapshot.child("preferredSex").getValue().toString();
                currentUserMinPrefAge = snapshot.child("minPrefAge").getValue().toString();
                currentUserMaxPrefAge = snapshot.child("maxPrefAge").getValue().toString();

                Log.d("SETTX", currentUserMinPrefAge);

                displayCurrentUsersInfo();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dbHelper.getCurrentUserReference().addValueEventListener(currentUsersDataValueEventListener);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadNewData()) {
                    addNewDataToDataBase();
                }
            }
        });

        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DELETEX", "Naciśnięto delete");
                Intent intent = new Intent(getContext(), DeleteAccountActivity.class);
                startActivity(intent);

            }
        });

        return rootView;
    }

    private void displayCurrentUsersInfo() {

        Log.d("SETTX", "jestem w display");

        nameTxt.setText(currentUserName);
        localisationTxt.setText(currentUserLocalisation);
        ageTxt.setText(currentUserAge);
        descriptionTxt.setText(currentUserDescription);
        minAgeTxt.setText(currentUserMinPrefAge);
        maxAgeTxt.setText(currentUserMaxPrefAge);

        if (currentUserPreferredSex.equals("both")) {
            femalesCB.setChecked(true);
            malesCB.setChecked(true);
        } else if (currentUserPreferredSex.equals("female")) {
            femalesCB.setChecked(true);
            malesCB.setChecked(false);
        } else if (currentUserPreferredSex.equals("male")) {
            femalesCB.setChecked(false);
            malesCB.setChecked(true);
        }
    }

    private boolean loadNewData() {
        currentUserName  = nameTxt.getText().toString().trim();
        currentUserLocalisation = localisationTxt.getText().toString().trim();
        currentUserAge = ageTxt.getText().toString().trim();
        currentUserDescription = descriptionTxt.getText().toString().trim();
        currentUserMinPrefAge = minAgeTxt.getText().toString().trim();
        currentUserMaxPrefAge = maxAgeTxt.getText().toString().trim();

        if(femalesCB.isChecked() && malesCB.isChecked()) {
            currentUserPreferredSex = "both";
        } else if(femalesCB.isChecked()) {
            currentUserPreferredSex = "female";
        } else if(malesCB.isChecked()) {
            currentUserPreferredSex = "male";
        }

        try {
            age = Integer.parseInt(currentUserAge); //przypisanie zmiennej age wartości wpisanej w pole ageEditText
            minAge = Integer.parseInt(currentUserMinPrefAge);
            maxAge = Integer.parseInt(currentUserMaxPrefAge);
        } catch (NumberFormatException exception) { //jeżeli wpisany text nie będzie liczbą, zostanie wyrzucony wyjątek
            if (age != 0 || minAge != 0 || maxAge != 0) {
                Toast.makeText(getContext(), "Incorrect age format.", Toast.LENGTH_LONG).show(); //komunikat informujący o wyrzuceniu wyjątku
            }
        }

        if (currentUserName.isEmpty() || currentUserLocalisation.isEmpty() || currentUserAge.isEmpty() || currentUserDescription.isEmpty() || currentUserMinPrefAge.isEmpty() || currentUserMaxPrefAge.isEmpty() || (!malesCB.isChecked() && !femalesCB.isChecked())) { //sprawdzenie czy wszystkie pola zostały uzupełnione
            Toast.makeText(getContext(), "Please add all required information.", Toast.LENGTH_SHORT).show(); //jeśli nie, wyświetlany jest komunikat
        } else {
            if (containsLettersOnly(currentUserName) && containsLettersOnly(currentUserLocalisation)) { //sprawdzenie czy do pól localisation zostały wprowadzone łańcuchy znaków zawierające tylko litery
                if (age >= 130 || age < 18 || minAge >= 130 || minAge < 18 || maxAge >= 130 || maxAge < 18 || (minAge >= maxAge)) { //sprawdzenie czy podany wiek jest ralistyczny i zgodny z przeznaczeniem aplikacji
                    Toast.makeText(getContext(), "Insert correct age, it must be greater or equal to 18.", Toast.LENGTH_LONG).show();
                } else { //jeżeli wszystkie powyższe warunki mają odpowiednie wartości

                    currentUserName = currentUserName.substring(0, 1).toUpperCase() + currentUserName.substring(1).toLowerCase(); //formatowanie imienia tak aby zaczynało się od wielkiej litery
                    currentUserLocalisation = currentUserLocalisation.substring(0, 1).toUpperCase() + currentUserLocalisation.substring(1).toLowerCase(); //formatowanie miasta tak aby zaczynało się od wielkiej litery
                    return true;
                }
            } else { //jeśli localisation i name składają się z innych znaków niż litery
                Toast.makeText(getContext(), "Name and localisation must contain only letters.", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }

    private void addNewDataToDataBase() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", currentUserName);
        map.put("localisation", currentUserLocalisation);
        map.put("age", currentUserAge);
        map.put("description", currentUserDescription);
        map.put("preferredSex", currentUserPreferredSex);
        map.put("minPrefAge", currentUserMinPrefAge);
        map.put("maxPrefAge", currentUserMaxPrefAge);
        dbHelper.getCurrentUserReference().updateChildren(map);
        Toast.makeText(getContext(), "Data updated", Toast.LENGTH_SHORT).show();
    }

    public boolean containsLettersOnly(String text) {
        char[] textCharacters = text.toCharArray();
        boolean isIt = true;

        for (char character : textCharacters) {
            if (!Character.isLetter(character)) {
                Log.d("VALID", character + " is NOT letter");
                isIt = false;
                return isIt;
            }
            Log.d("VALID", character + " is letter");
        }
        return isIt;
    }
}