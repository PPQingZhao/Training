package com.pp.media.callback;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OnBeanListChangedCallBack<Item, Bean> extends FullOnListChangeCallBack<ObservableList<Bean>> {
    private static final String TAG = "OnListChangeCallBack";

    private final ObservableList<Item> mItemList;

    private Adapter<Item, Bean> mAdapter;

    public OnBeanListChangedCallBack(@NonNull ObservableList<Item> list, @NonNull Adapter<Item, Bean> dapter) {
        this.mItemList = list;
        this.mAdapter = dapter;
    }

    @Override
    public void onItemRangeInserted(ObservableList<Bean> sender, int positionStart, int itemCount) {
//        Log.e(TAG, "onItemRangeInserted size:  " + sender.size() + "   start: " + positionStart + "   count: " + itemCount);
        Bean bean;
        Item item;
        List<Item> tempList = new ArrayList<>();
        for (int i = positionStart; i < sender.size(); i++) {
            bean = sender.get(i);

            item = mAdapter.createItem(bean);
            tempList.add(item);
        }

        mItemList.addAll(tempList);
    }

    @Override
    public void onItemRangeRemoved(ObservableList<Bean> sender, int positionStart, int itemCount) {
//        Log.e(TAG, "onItemRangeRemoved");

        List<Item> removeList = new ArrayList<>();
        for (int i = positionStart; i < itemCount; i++) {
            removeList.add(mItemList.get(i));
        }
        mItemList.removeAll(removeList);
    }

    public static abstract class Adapter<T, Bean> {

        public abstract T createItem(Bean bean);
    }
}
