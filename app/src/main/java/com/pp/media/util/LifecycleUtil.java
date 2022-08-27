package com.pp.media.util;


import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public interface LifecycleUtil {


    public abstract class FullLifecycleObserver implements LifecycleObserver {
        @OnLifecycleEvent(androidx.lifecycle.Lifecycle.Event.ON_CREATE)
        public void onCreate() {
        }

        @OnLifecycleEvent(androidx.lifecycle.Lifecycle.Event.ON_START)
        public void onStrat() {
        }

        @OnLifecycleEvent(androidx.lifecycle.Lifecycle.Event.ON_RESUME)
        public void onResume() {
        }

        @OnLifecycleEvent(androidx.lifecycle.Lifecycle.Event.ON_PAUSE)
        public void onPause() {
        }

        @OnLifecycleEvent(androidx.lifecycle.Lifecycle.Event.ON_STOP)
        public void onStop() {
        }

        @OnLifecycleEvent(androidx.lifecycle.Lifecycle.Event.ON_DESTROY)
        public void onDestrory() {
        }

        @OnLifecycleEvent(androidx.lifecycle.Lifecycle.Event.ON_ANY)
        public void onAny() {
        }
    }


}
