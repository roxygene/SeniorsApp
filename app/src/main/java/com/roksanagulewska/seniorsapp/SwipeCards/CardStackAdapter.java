package com.roksanagulewska.seniorsapp.SwipeCards;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.roksanagulewska.seniorsapp.DataBase.DataBaseHelper;
import com.roksanagulewska.seniorsapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<ItemModel> items;
    private DataBaseHelper dbHelper = new DataBaseHelper();
    //ItemModel currentItem;

    public CardStackAdapter(List<ItemModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));
        //currentItem = items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, age, localisation;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
            age = itemView.findViewById(R.id.item_age);
            localisation = itemView.findViewById(R.id.item_city);
        }

        void setData(ItemModel data) {
            if (data.getImageName().length() > 27) {
                Picasso.get()
                        .load(data.getImageUri())
                        .fit()
                        .centerCrop()
                        .rotate(90)
                        .into(image);
                name.setText(data.getName());
                age.setText(String.valueOf(data.getAge()));
                localisation.setText(data.getLocalisation());
            } else {
                Picasso.get()
                        .load(data.getImageUri())
                        .fit()
                        .centerCrop()
                        .into(image);
                name.setText(data.getName());
                age.setText(String.valueOf(data.getAge()));
                localisation.setText(data.getLocalisation());
            }
        }
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }
}
