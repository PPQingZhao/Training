package com.pp.media.lifecycle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

public class OnListCallbackHolderListener<Bean> extends LifecycleObjectHolder.OnHoldListener<ObservableList.OnListChangedCallback<ObservableList<Bean>>> {
    private static final String TAG = "OnListHolderListener";
    private final ObservableList<Bean> mList;

    public OnListCallbackHolderListener(@NonNull ObservableList<Bean> list) {
        this.mList = list;
    }


    @Override
    public void onHold(ObservableList.OnListChangedCallback<ObservableList<Bean>> callback) {
        Log.e(TAG, "onHold");
        mList.addOnListChangedCallback(callback);
    }

    @Override
    public void onRecycle(ObservableList.OnListChangedCallback<ObservableList<Bean>> callback) {
        Log.e(TAG, "onRecycle");
        mList.removeOnListChangedCallback(callback);
    }
}
