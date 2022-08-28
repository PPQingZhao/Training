package com.pp.mvvm.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;

import com.pp.mvvm.event.SingleLiveEvent;
import com.pp.mvvm.event.ViewEvent;

public class LifecycleViewModel extends AndroidViewModel implements DefaultLifecycleObserver {
    private final SingleLiveEvent<ViewEvent> mEventSender;

    public LifecycleViewModel(@NonNull Application application) {
        super(application);
        mEventSender = new SingleLiveEvent<>();
    }

    public SingleLiveEvent<ViewEvent> getEventSender() {
        return mEventSender;
    }

    public void setEvent(ViewEvent event){
        getEventSender().setValue(event);
    }
}
