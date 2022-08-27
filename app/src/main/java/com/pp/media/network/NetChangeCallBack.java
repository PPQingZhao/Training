package com.pp.media.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetChangeCallBack extends ConnectivityManager.NetworkCallback implements INetworkMonitor {

    private MutableLiveData<String> mStateObserver;

    @Override
    public void onLost(@NonNull Network network) {
        if (null != mStateObserver) {
            mStateObserver.setValue(NetworkChange.NETWORK_NONE);
        }
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {

        // 网络连接成功验证
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {

            String type = NetworkChange.NETWORK_UNKNOWN;
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                type = NetworkChange.NETWORK_WIFI;
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                type = NetworkChange.NETWORK_2G;
            } else {
                type = NetworkChange.NETWORK_UNKNOWN;
            }
            if (null != mStateObserver) {
                // 通知网络变化
                mStateObserver.postValue(type);
            }
        }

    }

    @Override
    public void start(@NonNull Context ctx, MutableLiveData<String> observer) {

        mStateObserver = observer;

        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        manager.registerNetworkCallback(new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .build()
                , this);
    }

    @Override
    public void end(@NonNull Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        manager.unregisterNetworkCallback(this);

    }
}
