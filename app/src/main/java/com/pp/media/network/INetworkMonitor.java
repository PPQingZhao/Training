package com.pp.media.network;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public interface INetworkMonitor {

    /**
     * 开始网络监察
     *
     * @param ctx
     * @param observer
     */
    void start(@NonNull Context ctx, MutableLiveData<String> observer);

    /**
     * 结束网路监察
     */
    void end(@NonNull Context ctx);

    interface Factory {
        <T extends INetworkMonitor> T create();
    }

}
