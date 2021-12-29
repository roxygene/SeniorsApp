package com.roksanagulewska.seniorsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.roksanagulewska.seniorsapp.Chat.ChatAdapter;
import com.roksanagulewska.seniorsapp.Chat.ChatModel;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.Fragments.MessagesFragment;
import com.roksanagulewska.seniorsapp.Matches.MatchesListAdapter;
import com.roksanagulewska.seniorsapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private ChatAdapter adapter;
    //private RecyclerView.LayoutManager manager;
    DataBaseHelper dbHelper = new DataBaseHelper();
    MaterialButton backBtn, profileBtn, deleteBtn, sendBtn;
    EditText messageEditTxt;
    TextView matchNameTxtView;
    List<ChatModel> messagesToDisplayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent messageIntent = getIntent();
        Bundle messageBundle = messageIntent.getExtras();
        String matchId = messageBundle.getString("matchId");
        String matchName = messageBundle.getString("matchName");



        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        //manager = new LinearLayoutManager(getApplicationContext());
        //chatRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);

        chatRecyclerView = findViewById(R.id.chat_recycler_view);


        backBtn = findViewById(R.id.backBtn);
        profileBtn = findViewById(R.id.profileBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        sendBtn = findViewById(R.id.sendBtn);
        messageEditTxt = findViewById(R.id.messageInput);
        matchNameTxtView = findViewById(R.id.matchNameTextView);




        matchNameTxtView.setText(matchName);
        readMessages(dbHelper.getCurrentUserId(), matchId);

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
                if(messageToSend.equals("")) {
                    Toast.makeText(getApplicationContext(), "You can't send an empty message!", Toast.LENGTH_SHORT).show();
                } else {
                    if(messageToSend.length() >= 251) {
                        Toast.makeText(getApplicationContext(), "Your message is too long! Type less than 250 characters.", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d("CHATX", "poprawnie");
                        sendMessage(dbHelper.getCurrentUserId(), matchId, messageToSend);
                        messageEditTxt.setText("");
                    }
                }
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        Log.d("CHATX", "jestm w sendmessage");
        Map<String, Object> map = new HashMap<>();
        map.put("sender", sender);
        map.put("receiver", receiver);
        map.put("message", message);
        dbHelper.getDatabaseReference().child("Chats").push().setValue(map);

    }

    private void readMessages(String currentUserId, String matchId) {
        messagesToDisplayList = new ArrayList<>();

        ValueEventListener readMessagesValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesToDisplayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatModel chat = dataSnapshot.getValue(ChatModel.class);
                    if (chat.getReceiver().equals(currentUserId) && chat.getSender().equals(matchId) ||
                            chat.getReceiver().equals(matchId) && chat.getSender().equals(currentUserId)) {
                        messagesToDisplayList.add(chat);
                    }
                    adapter = new ChatAdapter(messagesToDisplayList, ChatActivity.this);
                    chatRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dbHelper.getDatabaseReference().child("Chats").addValueEventListener(readMessagesValueEventListener);
    }
}