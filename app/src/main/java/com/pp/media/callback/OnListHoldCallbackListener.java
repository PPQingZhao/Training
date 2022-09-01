package com.pp.media.callback;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

public class OnListHoldCallbackListener<T, callback extends ObservableList.OnListChangedCallback<? extends ObservableList<T>>> extends RecycleCallbackHolder.OnHoldCallbackListener<callback> {
    private final ObservableList<T> mList;

    public OnListHoldCallbackListener(@NonNull ObservableList<T> list) {
        this.mList = list;
    }

    @Override
    public void onAddCallback(callback callback) {
        mList.addOnListChangedCallback(callback);
    }

    @Override
    public void onRemoveCallack(callback callack) {
        mList.removeOnListChangedCallback(callack);
    }
}