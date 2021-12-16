package com.roksanagulewska.seniorsapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.roksanagulewska.seniorsapp.SwipeCards.ItemMatchModel;
import com.roksanagulewska.seniorsapp.SwipeCards.ItemModel;

import java.util.List;

public class MatchesListAdapter extends ArrayAdapter<ItemMatchModel> {


    Context context;

    public MatchesListAdapter(Context context, int resourceId, List<ItemMatchModel> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

    }


}
