package com.roksanagulewska.seniorsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.Fragments.FindNewFriendsFragment;
import com.roksanagulewska.seniorsapp.Fragments.MessagesFragment;
import com.roksanagulewska.seniorsapp.Fragments.MyProfileFragment;
import com.roksanagulewska.seniorsapp.R;
import com.roksanagulewska.seniorsapp.Fragments.SettingsFragment;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    NavigationView navigationView;
    DataBaseHelper dbHelper = new DataBaseHelper();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState(); //rotacja hamburgera zależnie od tego czy szufladę nawigacji jest otwarta czy nie

        if (savedInstanceState == null) { //nie będzie się ładować przy każdym uruchomieniu onCreate
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FindNewFriendsFragment()).commit();
            //otwarcie frgmentu z szukaniem par przy starcie aktywności menu
            navigationView.setCheckedItem(R.id.nav_find_friends);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_my_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyProfileFragment()).commit();
                break;
            case R.id.nav_messages:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessagesFragment()).commit();
                break;
            case R.id.nav_find_friends:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FindNewFriendsFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_logout:
                logOut();
                break;
        }

        drawer.closeDrawer(GravityCompat.START); //zamknięcie szuflady po kliknięciu przycisku z menu
        return true;
    }

    //żeby klikając "wróć" nie wyjść z aktywności tylko zamknąć szufladę nawigacji nawigacji
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(NavigationActivity.this, StartingActivity.class);
        startActivity(intent);
    }
}