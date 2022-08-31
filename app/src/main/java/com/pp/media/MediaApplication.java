package com.pp.media;


import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.pp.mvvm.MvvmApplication;

public class MediaApplication extends MvvmApplication implements LifecycleObserver {


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
