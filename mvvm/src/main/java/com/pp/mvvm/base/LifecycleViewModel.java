package com.pp.mvvm.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;

import com.pp.mvvm.event.SingleLiveEvent;
import com.pp.mvvm.event.ViewEvent;

public class LifecycleViewModel extends AndroidViewModel implements LifecycleObserver {
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
