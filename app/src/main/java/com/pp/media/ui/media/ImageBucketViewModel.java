package com.pp.media.ui.media;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LifecycleOwner;

import com.pp.media.adapter.MultiltemAdapter;
import com.pp.media.adapter.OnItemListChangedCallback;
import com.pp.media.callback.LifecycleCallbackHolder;
import com.pp.media.callback.OnBeanListChangedCallBack;
import com.pp.media.callback.OnBeanListChangedCallBack.Adapter;
import com.pp.media.media.ImageBucket;
import com.pp.media.repository.MediaRepository;
import com.pp.media.ui.media.model.ImageBucketItemViewModel;
import com.pp.mvvm.base.LifecycleViewModel;
import com.pp.mvvm.event.ViewEvent;
import com.pp.mvvm.event.ViewEventHandler;

import java.util.List;

public class ImageBucketViewModel extends LifecycleViewModel {
    private static final String TAG = "ImageBucketViewModel";
    public final ObservableField<String> titleState = new ObservableField<>("Image Bucket");

    public ImageBucketViewModel(@NonNull Application application) {
        super(application);
    }

    private final ObservableList<ImageBucketItemViewModel> mItemList = new ObservableArrayList<>();

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        owner.getLifecycle().addObserver(mCallbackHolder);
    }

    private final LifecycleCallbackHolder<OnItemListChangedCallback<ImageBucketItemViewModel>>
            mCallbackHolder = new LifecycleCallbackHolder<>(new LifecycleCallbackHolder.OnHoldCallbackListener<OnItemListChangedCallback<ImageBucketItemViewModel>>() {
        @Override
        public void onAddCallback(OnItemListChangedCallback<ImageBucketItemViewModel> callback) {
            mItemList.addOnListChangedCallback(callback);
            Log.e(TAG, "callback");
        }

        @Override
        public void onRemoveCallack(OnItemListChangedCallback<ImageBucketItemViewModel> callack) {
            mItemList.removeOnListChangedCallback(callack);
        }

    });

    public void addOnItemChangedCallback(LifecycleOwner owner, OnItemListChangedCallback<ImageBucketItemViewModel> callback) {
        mCallbackHolder.holdCallback(owner, callback);
    }

    public void addOnImageBucketChangedCallback(@NonNull MediaRepository repository, LifecycleOwner owner) {
        repository.addOnImageBucketChangedCallback(owner, new OnBeanListChangedCallBack<ImageBucketItemViewModel, ImageBucket>(mItemList, mBucketAdapter));
    }

    final Adapter<ImageBucketItemViewModel, ImageBucket> mBucketAdapter = new Adapter<ImageBucketItemViewModel, ImageBucket>() {
        ImageBucketItemViewModel itemViewModel;

        @Override
        public ImageBucketItemViewModel createItem(ImageBucket imageBucket) {
            itemViewModel = new ImageBucketItemViewModel();
            itemViewModel.setImageBucket(imageBucket);
            return itemViewModel;
        }
    };

    public void setData(MultiltemAdapter<ImageBucketItemViewModel> adapter, List<ImageBucket> images) {
        ObservableList<ImageBucket> imageBuckets = new ObservableArrayList<>();

        OnBeanListChangedCallBack<ImageBucketItemViewModel, ImageBucket> callBack = new OnBeanListChangedCallBack<>(mItemList, mBucketAdapter);
        imageBuckets.addOnListChangedCallback(callBack);

        imageBuckets.addAll(images);

        imageBuckets.removeOnListChangedCallback(callBack);

        adapter.setNewData(mItemList);
    }
}
