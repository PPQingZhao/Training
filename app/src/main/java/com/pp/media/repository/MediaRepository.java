package com.pp.media.repository;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.pp.media.repository.bean.Media;
import com.pp.media.repository.datasource.LocalMediaDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MediaRepository implements DefaultLifecycleObserver {

    private List<IMediaDataSource> mDataSourceList;

    MediaRepository() {
    }

    public void addDataSource(@NonNull IMediaDataSource dataSource) {
        if (null != mDataSourceList) {
            if (!mDataSourceList.contains(dataSource)) {
                mDataSourceList.add(dataSource);
            }
        } else {
            mDataSourceList = new ArrayList<>();
            addDataSource(dataSource);
        }
    }

    private final ObservableList<Media> mList = new ObservableArrayList<>();

    final List<ObservableList.OnListChangedCallback<ObservableList<Media>>> callbacks = new ArrayList<ObservableList.OnListChangedCallback<ObservableList<Media>>>();

    public void addOnMediaListChangeCallBack(ObservableList.OnListChangedCallback<ObservableList<Media>> callback) {
        if (null != callback && !callbacks.contains(callback)) {
            callbacks.add(callback);
            mList.addOnListChangedCallback(callback);
        }
    }

    public void removeOnMediaListChangeCallBack(ObservableList.OnListChangedCallback<ObservableList<Media>> callback) {
        callbacks.remove(callback);
        mList.removeOnListChangedCallback(callback);
    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void load(@NonNull final Context ctx) {
        Disposable subscribe = stopLoad()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Object, ObservableSource<IMediaDataSource>>() {
                    @Override
                    public ObservableSource<IMediaDataSource> apply(Object o) throws Exception {
//                        Log.e("TAG", " apply  111111");
                        List<IMediaDataSource> dataSources = mDataSourceList == null || mDataSourceList.size() == 0 ? new ArrayList<IMediaDataSource>() : mDataSourceList;
                        return Observable.fromIterable(dataSources);
                    }
                })
                .flatMap(new Function<IMediaDataSource, ObservableSource<ObservableList<Media>>>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public ObservableSource<ObservableList<Media>> apply(IMediaDataSource dataSource) throws Exception {
                        return dataSource.load(ctx);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ObservableList<Media>>() {
                    @Override
                    public void accept(ObservableList<Media> media) throws Exception {
                        mList.addAll(media);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG",throwable.getMessage());
                    }
                });
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        stopLoad();
        for (ObservableList.OnListChangedCallback<ObservableList<Media>> callback : callbacks) {
//            Log.e("TAG","remove callback");
            mList.removeOnListChangedCallback(callback);
        }
        callbacks.clear();
    }

    public Observable<Object> stopLoad() {

        List<IMediaDataSource> dataSources = mDataSourceList == null || mDataSourceList.size() == 0 ? new ArrayList<IMediaDataSource>() : mDataSourceList;

        return Observable.fromIterable(dataSources)
                .map(new Function<IMediaDataSource, Object>() {
                    @Override
                    public Object apply(IMediaDataSource dataSource) throws Exception {
//                        Log.e("TAG", "apply stop");
                        dataSource.stop();
                        return dataSource;
                    }
                }).doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
//                        Log.e("TAG","on next  clear");
                        mList.clear();
                    }
                });
    }

    public static class MediaRepositoryFactory {
        public static MediaRepository create() {
            MediaRepository instance = new MediaRepository();
            instance.addDataSource(new LocalMediaDataSource());
            return instance;
        }
    }

}
