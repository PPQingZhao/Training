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
import androidx.lifecycle.LifecycleOwner;

import com.pp.media.callback.LifecycleCallbackHolder;
import com.pp.media.media.ImageBucket;
import com.pp.media.repository.datasource.LocalMediaDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MediaRepository implements DefaultLifecycleObserver {

    private static final String TAG = "MediaRepository";
    private List<IMediaDataSource> mDataSourceList;
    private Context mContext;

    MediaRepository() {
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        owner.getLifecycle().addObserver(mCallbackHelper);
    }

    @SuppressLint("MissingPermission")
    public void init(@NonNull Context context) {
        if (null != mContext) {
            throw new ExceptionInInitializerError("MediaRepository has been initialized");
        }
        mContext = context;
        loadImages();

    }

    public void addDataSource(@NonNull IMediaDataSource dataSource) {
        if (null != mDataSourceList) {
            if (!mDataSourceList.contains(dataSource)) {
                mDataSourceList.add(dataSource);
                load(dataSource);
            }
        } else {
            mDataSourceList = new ArrayList<>();
            addDataSource(dataSource);
        }
    }

    private final ObservableList<ImageBucket> mList = new ObservableArrayList<>();

    private final LifecycleCallbackHolder<ObservableList.OnListChangedCallback<ObservableList<ImageBucket>>> mCallbackHelper = new LifecycleCallbackHolder<>(new LifecycleCallbackHolder.OnHoldCallbackListener<ObservableList.OnListChangedCallback<ObservableList<ImageBucket>>>() {
        @Override
        public void onAddCallback(ObservableList.OnListChangedCallback<ObservableList<ImageBucket>> callback) {
            mList.addOnListChangedCallback(callback);
        }

        @Override
        public void onRemoveCallack(ObservableList.OnListChangedCallback<ObservableList<ImageBucket>> callack) {
            mList.removeOnListChangedCallback(callack);
        }
    });

    public void addOnImageBucketChangedCallback(LifecycleOwner owner, ObservableList.OnListChangedCallback<ObservableList<ImageBucket>> callback) {
        if (null != callback) {
            mCallbackHelper.holdCallback(owner, callback);
        }
    }

    public void removeOnMediaListChangeCallBack(ObservableList.OnListChangedCallback<ObservableList<ImageBucket>> callback) {
        mCallbackHelper.removeCallback(callback);
    }

    /**
     * 请确保已经init
     *
     * @return
     */
    public List<ImageBucket> getImages() {
        if (null == mContext) {
            throw new ExceptionInInitializerError("MediaRepository not initialized");
        }
        return Collections.unmodifiableList(mList);
    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void loadImages() {
        Disposable subscribe = stopLoad()
                .flatMap(new Function<Object, ObservableSource<IMediaDataSource>>() {
                    @Override
                    public ObservableSource<IMediaDataSource> apply(Object o) throws Exception {
//                        Log.e("TAG", " apply  111111");
                        return Observable.fromIterable(new ArrayList<>(mDataSourceList));
                    }
                })
                .subscribe(new Consumer<IMediaDataSource>() {
                    @Override
                    public void accept(IMediaDataSource dataSource) throws Exception {
                        load(dataSource);
                    }
                });
    }

    private void load(IMediaDataSource dataSource) {

        if (null == mContext) {
            return;
        }

        Disposable disposable = Observable.just(dataSource)
                .flatMap(new Function<IMediaDataSource, ObservableSource<ObservableList<ImageBucket>>>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public ObservableSource<ObservableList<ImageBucket>> apply(IMediaDataSource dataSource) throws Exception {
//                        Log.e(TAG, Thread.currentThread().toString());
                        return dataSource.load(mContext);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ObservableList<ImageBucket>>() {
                    @Override
                    public void accept(ObservableList<ImageBucket> buckets) throws Exception {
                        mList.addAll(buckets);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
    }

    public Observable<Object> stopLoad() {

        return Observable.fromIterable(mDataSourceList == null ? new ArrayList<IMediaDataSource>() : mDataSourceList)
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
