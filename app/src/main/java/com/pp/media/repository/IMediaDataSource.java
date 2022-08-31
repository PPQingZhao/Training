package com.pp.media.repository;

import android.Manifest;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LiveData;


import com.pp.media.media.ImageBucket;
import com.pp.media.repository.bean.Media;


import io.reactivex.Observable;

public interface IMediaDataSource {

    /**
     * 加载media
     *
     * @param ctx
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    Observable<ObservableList<Media>> load1(@NonNull Context ctx);

    Observable<ObservableList<ImageBucket>> load(@NonNull final Context ctx);

    /**
     * 停止加载
     *
     * @return
     */
    void stop();
}
