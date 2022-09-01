package com.pp.media.callback;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来帮助回收callbcall
 * @param <callback>
 */
public class RecycleCallbackHolder<callback> implements DefaultLifecycleObserver {

    private static final String TAG = "RecycleCallbackHolder";
    private final ArrayMap<LifecycleOwner, Holder<callback>> mHolderMap = new ArrayMap<>();
    private OnHoldCallbackListener<callback> mAdapter;

    public RecycleCallbackHolder(@NonNull OnHoldCallbackListener<callback> adapter) {
        this.mAdapter = adapter;
    }

    public void setAdapter(@NonNull OnHoldCallbackListener<callback> adapter) {
        this.mAdapter = adapter;
    }

    public void holdCallback(LifecycleOwner owner, callback callback) {
        // 添加生命周期感应
        owner.getLifecycle().addObserver(this);

        Holder<callback> holder = getHolder(owner);
        holder.add(callback);
        mAdapter.onAddCallback(callback);
    }

    public void removeCallback(LifecycleOwner owner) {
        List<callback> callbacks = new ArrayList<>();
        if (mHolderMap.get(owner) != null) {
            callbacks.addAll(mHolderMap.get(owner).mHolders);
            for (callback c : callbacks) {
                removeCallback(c);
            }
        }
        mHolderMap.remove(owner);
    }

    public void removeCallback(callback callack) {
        for (Holder<callback> holder : mHolderMap.values()) {
            if (holder.remove(callack)) {
                mAdapter.onRemoveCallack(callack);
            }
        }
    }

    private Holder<callback> getHolder(LifecycleOwner owner) {

        Holder<callback> holder = mHolderMap.get(owner);
        if (null == holder) {
            holder = new Holder<callback>();
            mHolderMap.put(owner, holder);
        }
        return holder;
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        removeCallback(owner);
//        Log.e(TAG, "destroy: " + owner.toString());
//        Log.e(TAG, "destroy: " + mHolderMap.size());
    }

    private static class Holder<T> {
        private List<T> mHolders;

        private boolean add(T t) {
            if (null == mHolders) {
                mHolders = new ArrayList<>();
            }
            if (!mHolders.contains(t)) {
                return mHolders.add(t);
            }
            return false;
        }

        private boolean remove(T t) {
            if (null != mHolders) {
                return mHolders.remove(t);
            }
            return false;
        }
    }

    public static abstract class OnHoldCallbackListener<T> {

        public abstract void onAddCallback(T callback);

        public abstract void onRemoveCallack(T callack);
    }
}
