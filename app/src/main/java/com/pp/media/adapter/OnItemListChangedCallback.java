package com.pp.media.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;


public class OnItemListChangedCallback<T extends BaseAbstractExpandleItem> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    private MultiltemAdapter<T> mAdapter;

    public OnItemListChangedCallback(@NonNull MultiltemAdapter<T> adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void onChanged(ObservableList<T> sender) {
        mAdapter.setNewData(sender);
    }

    @Override
    public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
//        Log.e("TAG", "size: " + sender.size() + "      positionStart: " + positionStart + " count:  " + itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
        mAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }
}
