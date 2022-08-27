package com.pp.media.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


public class NetChangeReceiver extends BroadcastReceiver implements INetworkMonitor {

    private MutableLiveData<String> mStateObserver;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            return;
        }

        String networkType = NetworkChange.NETWORK_UNKNOWN;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null != activeNetwork && activeNetwork.isConnected()) {
            int type = activeNetwork.getType();
            if (ConnectivityManager.TYPE_WIFI == type) {
                networkType = NetworkChange.NETWORK_WIFI;
            } else if (ConnectivityManager.TYPE_MOBILE == type) {
                switch (activeNetwork.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        networkType = NetworkChange.NETWORK_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                    case TelephonyManager.NETWORK_TYPE_IWLAN:
                        networkType = NetworkChange.NETWORK_4G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        networkType = NetworkChange.NETWORK_2G;
                        break;
                    default:
                        String subName = activeNetwork.getSubtypeName();
                        if (subName.equalsIgnoreCase("TD-SCDMA")
                                || subName.equalsIgnoreCase("WCDMA")
                                || subName.equalsIgnoreCase("CDMA2000")) {
                            networkType = NetworkChange.NETWORK_3G;
                        } else {
                            networkType = NetworkChange.NETWORK_UNKNOWN;
                        }
                }

            }

        } else {
            networkType = NetworkChange.NETWORK_NONE;
        }

        //通知网络发生改变
        if (null != mStateObserver) {
            mStateObserver.postValue(networkType);
        }
    }

    @Override
    public void start(@NonNull Context ctx, MutableLiveData<String> observer) {
        mStateObserver = observer;
        ctx.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void end(@NonNull Context ctx) {
        ctx.unregisterReceiver(this);
    }
}
