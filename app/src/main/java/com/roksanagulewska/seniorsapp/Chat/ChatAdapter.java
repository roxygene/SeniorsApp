package com.roksanagulewska.seniorsapp.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roksanagulewska.seniorsapp.Activities.ChatActivity;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    public static final  int MSG_TYPE_CURRENT_USER = 0;
    public static final  int MSG_TYPE_MATCH = 1;
    private List<ChatModel> messagesToDisplayList;
    Context context;
    DataBaseHelper dbHelper = new DataBaseHelper();

    public ChatAdapter(List<ChatModel> messagesToDisplayList, Context context) {
        this.messagesToDisplayList = messagesToDisplayList;
        this.context = context;
        Log.d("CHATX", "chat adapter");
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("CHATX", "chatViewHolder");
        if (viewType == MSG_TYPE_CURRENT_USER) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_chat_current_user, parent, false);
            return new ChatViewHolder(view);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_chat_match, parent, false);
            return new ChatViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Log.d("CHATX", "chat onBindViewHolder");
        ChatModel chat  = messagesToDisplayList.get(position);
        holder.showMessage.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return messagesToDisplayList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView showMessage;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.show_message);

            Log.d("CHATX", "chatViewHolder");
        }



    }

    @Override
    public int getItemViewType(int position) {
        if (messagesToDisplayList.get(position).getSender().equals(dbHelper.getCurrentUserId())) {
            return  MSG_TYPE_CURRENT_USER;
        } else {
            return MSG_TYPE_MATCH;
        }
    }
}
