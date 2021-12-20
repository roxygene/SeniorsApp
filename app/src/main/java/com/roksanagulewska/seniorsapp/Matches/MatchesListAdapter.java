package com.roksanagulewska.seniorsapp.Matches;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.roksanagulewska.seniorsapp.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MatchesListAdapter extends RecyclerView.Adapter<MatchesListAdapter.MatchesViewHolder> {
    private List<ItemMatchModel> friendsToDisplayList; //lista elementów wyświetlanych przez adapter
    Context context;

    public MatchesListAdapter(List<ItemMatchModel> friendsToDisplayList, Context context) {
        this.friendsToDisplayList = friendsToDisplayList;
        this.context = context;
        Log.d("MATCHX", "cooooom");
    }

    @NonNull
    @Override
    public MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MATCHX", "cooooom1");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_match, parent, false);
        return new MatchesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolder holder, int position) {
        holder.setData(friendsToDisplayList.get(position));
        Log.d("MATCHX", "cooooom2");
    }

    @Override
    public int getItemCount() {
        return friendsToDisplayList.size();
    }

    class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        MatchesViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.item_match_image);
            name = itemView.findViewById(R.id.item_match_name);
            Log.d("MATCHX", "matchesViewHolder");

            itemView.setOnClickListener(this);
        }

        void setData(ItemMatchModel data) {
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
        }


        @Override
        public void onClick(View v) {
            Log.d("MATCHX", "przycisk");
        }

    }
}
