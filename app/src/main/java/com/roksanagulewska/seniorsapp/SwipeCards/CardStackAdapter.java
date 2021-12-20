package com.roksanagulewska.seniorsapp.SwipeCards;

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

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<ItemModel> items; //lista elementów wyświetlanych przez adapter

    public CardStackAdapter(List<ItemModel> items) { //konstruktor klasy CardStackAdapter
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MATCHX", "cooooof1");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));
        Log.d("MATCHX", "cooooof2");
    }

    @Override
    public int getItemCount() {
        return items.size();
    } //metoda sprawdzająca ilość elementów do wyświetlenia

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, age, localisation, description;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
            age = itemView.findViewById(R.id.item_age);
            localisation = itemView.findViewById(R.id.item_city);
            description = itemView.findViewById(R.id.item_description);
        }

        void setData(ItemModel data) {
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
            age.setText(String.valueOf(data.getAge()));
            localisation.setText(data.getLocalisation());
            description.setText(data.getDescription());
        }
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }
}
