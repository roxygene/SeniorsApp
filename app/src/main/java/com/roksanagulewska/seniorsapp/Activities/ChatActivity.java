package com.roksanagulewska.seniorsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.roksanagulewska.seniorsapp.Chat.ChatAdapter;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.Fragments.MessagesFragment;
import com.roksanagulewska.seniorsapp.Matches.MatchesListAdapter;
import com.roksanagulewska.seniorsapp.R;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private ChatAdapter adapter;
    private RecyclerView.LayoutManager manager;
    DataBaseHelper dbHelper = new DataBaseHelper();

    MaterialButton backBtn, profileBtn, deleteBtn, sendBtn;
    EditText messageEditTxt;
    TextView matchNameTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent messageIntent = getIntent();
        Bundle messageBundle = messageIntent.getExtras();
        String matchId = messageBundle.getString("matchId");
        String matchName = messageBundle.getString("matchName");

        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        manager = new LinearLayoutManager(getApplicationContext());

        backBtn = findViewById(R.id.backBtn);
        profileBtn = findViewById(R.id.profileBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        sendBtn = findViewById(R.id.sendBtn);
        messageEditTxt = findViewById(R.id.messageInput);
        matchNameTxtView = findViewById(R.id.matchNameTextView);

        matchNameTxtView.setText(matchName);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(), MessagesFragment.class);
                startActivity(backIntent);
                finish();
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent matchProfileIntent = new Intent(getApplicationContext(), MatchProfileActivity.class);
                Bundle matchProfileBundle = new Bundle();
                matchProfileBundle.putString("matchId", matchId);
                matchProfileIntent.putExtras(matchProfileBundle);
                startActivity(matchProfileIntent);
                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteMatchIntent = new Intent(getApplicationContext(), DeleteMatchActivity.class);
                Bundle deleteBundle = new Bundle();
                deleteBundle.putString("matchId", matchId);
                deleteMatchIntent.putExtras(deleteBundle);
                startActivity(deleteMatchIntent);
                finish();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageToSend = messageEditTxt.getText().toString().trim();

                //.....
            }
        });
    }
}