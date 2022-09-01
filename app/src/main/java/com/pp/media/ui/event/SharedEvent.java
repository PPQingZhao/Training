package com.pp.media.ui.event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.pp.media.callback.RecycleCallbackHolder;
import com.pp.mvvm.event.ViewEvent;

public final class SharedEvent {

    private static final String TAG = "SharedEvent";

    private final RecycleCallbackHolder<Observer<ViewEvent>> mCallbackHolder;
    private final MutableLiveData<ViewEvent> mLiveData;

    private SharedEvent() {
        mLiveData = new MutableLiveData<>();
        mCallbackHolder = new RecycleCallbackHolder<Observer<ViewEvent>>(new RecycleCallbackHolder.OnHoldCallbackListener<Observer<ViewEvent>>() {
            @Override
            public void onAddCallback(Observer<ViewEvent> callback) {
                mLiveData.observeForever(callback);
            }

            @Override
            public void onRemoveCallack(Observer<ViewEvent> callack) {
                mLiveData.removeObserver(callack);
            }
        });
    }

    public <Data> void sendEvent(ViewEvent.DataOwner<Data> dataOwner, String eventType) {
        mLiveData.setValue(new ViewEvent<Data>(dataOwner, eventType));
    }

    private static class Holder {
        private static final SharedEvent instance = new SharedEvent();
    }

    public static SharedEvent getInstance() {
        return Holder.instance;
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<ViewEvent> observer) {
        mCallbackHolder.holdCallback(owner, observer);
    }

}
