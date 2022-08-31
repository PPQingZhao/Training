package com.pp.media.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.pp.mvvm.event.SingleLiveEvent;
import com.pp.mvvm.event.ViewEvent;

public final class SharedLiveEvent extends SingleLiveEvent<ViewEvent> implements DefaultLifecycleObserver {

    private static final String TAG = "SharedLiveEvent";

    private static final SharedLiveEvent sInstance = new SharedLiveEvent();

    private SharedLiveEvent() {
    }

    public static SharedLiveEvent getInstance() {
        return sInstance;
    }

    static final String CLEAR_TYPE = "clear data on destroy";
    static final ViewEvent sClearData = new ViewEvent(CLEAR_TYPE);

    @Override
    public void observe(LifecycleOwner owner, Observer<? super ViewEvent> observer) {

        owner.getLifecycle().addObserver(this);
        super.observe(owner, new Observer<ViewEvent>() {
            @Override
            public void onChanged(ViewEvent t) {
                if (null != t && CLEAR_TYPE.equals(t.getType())) {
                    return;
                }
                observer.onChanged(t);
            }
        });
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        setValue(sClearData);
    }
}
