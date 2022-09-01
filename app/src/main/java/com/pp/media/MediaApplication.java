package com.pp.media;


import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.pp.mvvm.MvvmApplication;

public class MediaApplication extends MvvmApplication implements LifecycleObserver {

    private static MediaApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MediaApplication getInstance() {
        return sInstance;
    }
}
