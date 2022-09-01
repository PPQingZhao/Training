package com.pp.media.callback;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

import com.pp.media.adapter.BaseAbstractExpandleItem;
import com.pp.media.adapter.MultiltemAdapter;


public class OnItemListChangedCallback<T extends BaseAbstractExpandleItem> extends ObservableList.OnListChangedCallback<ObservableList<T>> {
    private static final String TAG = "OnItemListChanged";
    private MultiltemAdapter<T> mAdapter;

    public OnItemListChangedCallback(@NonNull MultiltemAdapter<T> adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void onChanged(ObservableList<T> sender) {
        mAdapter.notifyDataSetChanged();
//        Log.e(TAG, "onChanged  size: " + sender.size());
    }

    @Override
    public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeChanged(positionStart, itemCount);
//        Log.e(TAG, "onItemRangeChanged  size: " + sender.size());
    }

    @Override
    public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
//        Log.e(TAG, "onItemRangeInserted   size: " + sender.size() + "      positionStart: " + positionStart + " count:  " + itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
        mAdapter.notifyItemMoved(fromPosition, toPosition);
//        Log.e(TAG, "onItemRangeInserted  size: " + sender.size());
    }

    @Override
    public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
        mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
//        Log.e(TAG, "onItemRangeRemoved  size: " + sender.size());
    }
}
