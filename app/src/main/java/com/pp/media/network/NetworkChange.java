package com.pp.media.network;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class NetworkChange implements DefaultLifecycleObserver {

    /**
     * 无网络
     */
    public final static String NETWORK_NONE = "network none";
    public final static String NETWORK_WIFI = "wifi";
    public final static String NETWORK_4G = "4G";
    public final static String NETWORK_3G = "3G";
    public final static String NETWORK_2G = "2G";
    public final static String NETWORK_5G = "5G";
    public final static String NETWORK_UNKNOWN = "unknown";


    @StringDef({NETWORK_NONE,
            NETWORK_WIFI,
            NETWORK_2G,
            NETWORK_3G,
            NETWORK_4G,
            NETWORK_5G,
            NETWORK_UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkState {
    }


    private final MutableLiveData<String> mState;

    private INetworkMonitor.Factory mFactory;

    private INetworkMonitor mMonitor;
    private Context mCtx;

    public NetworkChange(@NonNull INetworkMonitor.Factory factory) {
        this.mFactory = factory;

        mState = new MutableLiveData<>();
    }

    /**
     * 开始网络变化监听
     *
     * @param ctx
     * @return
     */
    public LiveData<String> startObserve(@NonNull Context ctx) {

        if (null != mMonitor) {
            mMonitor.end(ctx);
        }

        mCtx = ctx;
        mMonitor = mFactory.create();
        if (null == mMonitor) {
            throw new NullPointerException("monitor must not be null");
        }
        mMonitor.start(ctx, mState);
        return mState;
    }


    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {

        endObserve(mCtx);
        mCtx = null;
    }

    /**
     * 结束网络变化监听
     *
     * @param ctx
     */
    public void endObserve(@NonNull Context ctx) {
        mMonitor.end(ctx);
    }

    public static class Factory implements INetworkMonitor.Factory {
        @Override
        public INetworkMonitor create() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return new NetChangeCallBack();
            } else {
                return new NetChangeReceiver();
            }

        }
    }
}
