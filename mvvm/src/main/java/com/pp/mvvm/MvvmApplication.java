package com.pp.mvvm;

import android.app.Application;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

public class MvvmApplication extends Application implements LifecycleObserver {

    @Override
    public void onCreate() {
        super.onCreate();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


}
