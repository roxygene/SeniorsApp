package com.roksanagulewska.seniorsapp.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.DataBase.User;
import com.roksanagulewska.seniorsapp.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {

    ImageView imageView;
    TextView nameTxt, localisationTxt, ageTxt, descriptionTxt;
    DataBaseHelper dbHelper = new DataBaseHelper();
    String currentUserName;
    String currentUserLocalisation;
    String currentUserAge;
    String currentUserDescription;
    String currentUserImageName;
    String currentUserImageUri;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        Log.d("MYPROF", "my profileONCRV");

        imageView = rootView.findViewById(R.id.usersImage);
        nameTxt = rootView.findViewById(R.id.usersName);
        localisationTxt = rootView.findViewById(R.id.usersLocalisation);
        ageTxt = rootView.findViewById(R.id.usersAge);
        descriptionTxt = rootView.findViewById(R.id.usersDescription);

        ValueEventListener currentUsersDataValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()) {
                    currentUserName = snapshot.child("name").getValue().toString();
                    currentUserLocalisation = snapshot.child("localisation").getValue().toString();
                    currentUserAge = snapshot.child("age").getValue().toString();
                    currentUserDescription = snapshot.child("description").getValue().toString();
                    currentUserImageName = snapshot.child("mainPictureName").getValue().toString();
                    currentUserImageUri = snapshot.child("imageUri").getValue().toString();

                    displayCurrentUsersInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dbHelper.getCurrentUserReference().addValueEventListener(currentUsersDataValueEventListener);


        return rootView;
    }

    private void displayCurrentUsersInfo() {
        if (currentUserImageName.length() > 27) {
            Picasso.get()
                    .load(currentUserImageUri)
                    .fit()
                    .centerCrop()
                    .rotate(90) //jeżeli to zdjęce wykonane aparatem to obróć o 90 stopni
                    .into(imageView);

        } else {
            Picasso.get()
                    .load(currentUserImageUri)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }
        nameTxt.setText(currentUserName);
        localisationTxt.setText(currentUserLocalisation);
        ageTxt.setText(currentUserAge);
        descriptionTxt.setText(currentUserDescription);
    }
}