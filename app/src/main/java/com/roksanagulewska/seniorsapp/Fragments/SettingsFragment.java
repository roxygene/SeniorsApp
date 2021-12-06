package com.roksanagulewska.seniorsapp.Fragments;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    ImageView pictureView;
    Button cameraBtn, galleryBtn, confirmBtn, deleteAccountBtn;
    EditText nameTxt, localisationTxt, ageTxt, descriptionTxt, minAgeTxt, maxAgeTxt;
    CheckBox femalesCB, malesCB;

    DataBaseHelper dbHelper = new DataBaseHelper();

    String currentUserName;
    String currentUserLocalisation;
    String currentUserAge;
    String currentUserDescription;
    String currentUserImageName;
    String currentUserImageUri;
    String currentUserPreferredSex;
    String currentUserMinPrefAge;
    String currentUserMaxPrefAge;


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

        pictureView = rootView.findViewById(R.id.usersImage);
        cameraBtn = rootView.findViewById(R.id.cameraButton);
        galleryBtn = rootView.findViewById(R.id.galleryButton);
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
                currentUserImageName = snapshot.child("mainPictureName").getValue().toString();
                currentUserImageUri = snapshot.child("imageUri").getValue().toString();
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

        return rootView;
    }

    private void displayCurrentUsersInfo() {

        Log.d("SETTX", "jestem w display");

        if (currentUserImageName.length() > 27) {
            Picasso.get()
                    .load(currentUserImageUri)
                    .fit()
                    .centerCrop()
                    .rotate(90) //jeżeli to zdjęce wykonane aparatem to obróć o 90 stopni
                    .into(pictureView);

        } else {
            Picasso.get()
                    .load(currentUserImageUri)
                    .fit()
                    .centerCrop()
                    .into(pictureView);
        }
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
}