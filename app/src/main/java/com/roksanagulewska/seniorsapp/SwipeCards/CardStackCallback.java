package com.roksanagulewska.seniorsapp.SwipeCards;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class CardStackCallback extends DiffUtil.Callback {

    private List<ItemModel> oldCard, newCard;//stara karta, nowa karta

    public CardStackCallback(List<ItemModel> oldCard, List<ItemModel> newCard) {
        this.oldCard = oldCard;
        this.newCard = newCard;
    }

    @Override
    public int getOldListSize() {
        return oldCard.size();
    }

    @Override
    public int getNewListSize() {
        return newCard.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return (oldCard.get(oldItemPosition).getEmail()).equals(newCard.get(newItemPosition).getEmail());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCard.get(oldItemPosition) == newCard.get(newItemPosition);
    }
}
