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
import com.roksanagulewska.seniorsapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatModel> friendsToDisplayList; //lista elementów wyświetlanych przez adapter
    Context context;

    public ChatAdapter(List<ChatModel> friendsToDisplayList, Context context) {
        this.friendsToDisplayList = friendsToDisplayList;
        this.context = context;
        Log.d("MATCHX", "cooooom");
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MATCHX", "cooooom1");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_match, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.setData(friendsToDisplayList.get(position));
        Log.d("MATCHX", "cooooom2");
    }

    @Override
    public int getItemCount() {
        return friendsToDisplayList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;

        String clickedUserName;
        String clickedUserId;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_match_image);
            name = itemView.findViewById(R.id.item_match_name);
            Log.d("MATCHX", "matchesViewHolder");

            itemView.setOnClickListener(this);
        }

        void setData(ChatModel data) {
            Log.d("MATCHX", "setData");
            if (data.getImageName().length() > 27) {
                Picasso.get()//wyświetlanie obrazu za pomocą biblioteki Picasso
                        .load(data.getImageUri())
                        .fit()
                        .centerCrop()
                        .rotate(90) //jeżeli to zdjęce wykonane aparatem to obróć o 90 stopni
                        .into(image);
            } else {
                Picasso.get()
                        .load(data.getImageUri())
                        .fit()
                        .centerCrop()
                        .into(image);
            }
            name.setText(data.getName());

            clickedUserId = data.getUserId();
            clickedUserName = data.getName();

        }


        @Override
        public void onClick(View v) {
            Log.d("MATCHX", "przycisk");
            Log.d("MATCHX", "NAMEEEE: " + clickedUserName);

            Intent chatIntent = new Intent(context, ChatActivity.class);
            Bundle chatBundle = new Bundle();
            chatBundle.putString("matchId", clickedUserId);
            chatIntent.putExtras(chatBundle);
            context.startActivity(chatIntent);


        }

    }
}
