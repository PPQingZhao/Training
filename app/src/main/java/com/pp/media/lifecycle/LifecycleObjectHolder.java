package com.pp.media.lifecycle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;

public final class LifecycleObjectHolder<T> {

    private static final String TAG = "LifecycleObjectHolder";

    public LifecycleObjectHolder() {
    }


    public void holde(@NonNull LifecycleOwner owner, T t, @NonNull OnHoldListener<T> listener) {
        Log.e(TAG, "holde DESTROYED:  " + (owner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED));
        if (owner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }

        LifecycleObject lifecycleObject = listener.find(owner.getLifecycle());
        if (null != lifecycleObject) {
            return;
        }

        listener.addLifecycleObject(new LifecycleObject(owner.getLifecycle(), t, listener));
    }


    public class LifecycleObject implements DefaultLifecycleObserver {

        private final Lifecycle mLifecycle;
        private T mObject;
        private OnHoldListener<T> mListener;

        private LifecycleObject(@NonNull Lifecycle lifecycle, T object, OnHoldListener<T> listener) {
            this.mLifecycle = lifecycle;
            this.mObject = object;
            this.mListener = listener;
            ;
            // 感应生命周期
            lifecycle.addObserver(this);

            mListener.onHold(mObject);
        }

        @Override
        public void onDestroy(@NonNull LifecycleOwner owner) {
            Log.e("TAG", "LifecycleObject  onDestoy");
            recycle();
        }

        private void recycle() {
            Log.e("TAG", "LifecycleObject  recycle");
            mLifecycle.removeObserver(this);
            if (null != this.mListener) {
                mListener.removeLifecycleObject(this);
                mListener.onRecycle(mObject);
                mObject = null;
                this.mListener = null;
            }
        }
    }

    public static abstract class OnHoldListener<T> {
        private final List<LifecycleObjectHolder<T>.LifecycleObject> mLifecycleObjects;

        protected OnHoldListener() {
            mLifecycleObjects = new ArrayList<>();
        }

        public abstract void onHold(T t);

        public abstract void onRecycle(T t);

        void addLifecycleObject(LifecycleObjectHolder<T>.LifecycleObject object) {
            this.mLifecycleObjects.add(object);
        }

        void removeLifecycleObject(LifecycleObjectHolder<T>.LifecycleObject object) {
            Log.e("TAG", "OnHoldListener  removeLifecycleObject");
            mLifecycleObjects.remove(object);
        }

        public final void recycle(Lifecycle lifecycle) {

            Log.e("TAG", "OnHoldListener  recycle");
            LifecycleObjectHolder<T>.LifecycleObject lifecycleObject = find(lifecycle);
            if (null != lifecycleObject) {
                lifecycleObject.recycle();

                removeLifecycleObject(lifecycleObject);
                recycle(lifecycle);
            }
        }

        LifecycleObjectHolder<T>.LifecycleObject find(Lifecycle lifecycle) {
            LifecycleObjectHolder<T>.LifecycleObject lifecycleObject = null;
            if (null == lifecycle) {
                return lifecycleObject;
            }
            for (LifecycleObjectHolder<T>.LifecycleObject lo : mLifecycleObjects) {
                if (lifecycle == lo.mLifecycle) {
                    lifecycleObject = lo;
                    break;
                }
            }
            return lifecycleObject;
        }

        public final void recycle() {
            for (LifecycleObjectHolder<T>.LifecycleObject lifecycleObject : mLifecycleObjects) {
                lifecycleObject.recycle();
            }
        }
    }
}
