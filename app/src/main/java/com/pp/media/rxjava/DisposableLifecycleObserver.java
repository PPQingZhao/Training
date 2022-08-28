package com.pp.media.rxjava;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public abstract class DisposableLifecycleObserver implements DefaultLifecycleObserver {

    private final List<Disposable> mList = new ArrayList<>();

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        for (Disposable d : mList) {
            if (!d.isDisposed()) {
                d.dispose();
            }
        }
        mList.clear();
    }

    public void addDisposale(Disposable disposable) {
        if (null != disposable) {
            mList.add(disposable);
        }
    }

    public static DisposableLifecycleObserver newInstance() {
        return new DisposableLifecycleObserver() {
        };
    }

}
