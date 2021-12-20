package com.roksanagulewska.seniorsapp.Matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.roksanagulewska.seniorsapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchesListAdapter extends ArrayAdapter<ItemMatchModel> {
    List<ItemMatchModel> friendsToDisplayList;

    ImageView image;
    TextView name;

    public MatchesListAdapter(Context context, List<ItemMatchModel> friendsToDisplayList) {
        super(context, R.layout.item_match, friendsToDisplayList);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ItemMatchModel itemMatch = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_match, parent, false);
        }

        image = convertView.findViewById(R.id.item_match_image);
        name = convertView.findViewById(R.id.item_match_name);

        setData(friendsToDisplayList.get(position));

        return super.getView(position, convertView, parent);
    }

    /*@Override
    public void onBindViewHolder(@NonNull CardStackAdapter.ViewHolder holder, int position) {
        holder.setData(items.get(position));
        //currentItem = items.get(position);
    }*/

    void setData(ItemMatchModel data) {
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
}
