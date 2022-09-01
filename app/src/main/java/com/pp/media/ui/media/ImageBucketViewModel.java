package com.pp.media.ui.media;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.pp.media.adapter.MultiltemAdapter;
import com.pp.media.callback.OnBeanListChangedCallBack;
import com.pp.media.callback.OnBeanListChangedCallBack.Adapter;
import com.pp.media.callback.OnListHoldCallbackListener;
import com.pp.media.callback.RecycleCallbackHolder;
import com.pp.media.media.ImageBucket;
import com.pp.media.repository.MediaRepository;
import com.pp.media.ui.event.MediaEvent;
import com.pp.media.ui.media.model.ImageBucketItemViewModel;
import com.pp.mvvm.base.LifecycleViewModel;

import java.util.List;

public class ImageBucketViewModel extends LifecycleViewModel {
    private static final String TAG = "ImageBucketViewModel";
    public final ObservableField<String> titleState = new ObservableField<>("Image Bucket");
    private MutableLiveData<MediaEvent> mItemCallbackSender;

    public ImageBucketViewModel(@NonNull Application application) {
        super(application);
    }

    private final ObservableList<ImageBucketItemViewModel> mItemList = new ObservableArrayList<>();

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
    }

    private final RecycleCallbackHolder<ObservableList.OnListChangedCallback<ObservableList<ImageBucketItemViewModel>>>
            mCallbackHolder = new RecycleCallbackHolder<>(new OnListHoldCallbackListener<>(mItemList));

    public void addOnItemChangedCallback(LifecycleOwner owner, ObservableList.OnListChangedCallback<ObservableList<ImageBucketItemViewModel>> callback) {
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
            itemViewModel.setCallbackSender(mItemCallbackSender);
            return itemViewModel;
        }
    };

    public void setData(MultiltemAdapter<ImageBucketItemViewModel> adapter, List<ImageBucket> images) {

        if (mItemList.size() > 0) {
            return;
        }
        mItemList.clear();
        ObservableList<ImageBucket> imageBuckets = new ObservableArrayList<>();

        OnBeanListChangedCallBack<ImageBucketItemViewModel, ImageBucket> callBack = new OnBeanListChangedCallBack<>(mItemList, mBucketAdapter);
        imageBuckets.addOnListChangedCallback(callBack);

        imageBuckets.addAll(images);

        imageBuckets.removeOnListChangedCallback(callBack);

        adapter.setNewData(mItemList);
    }

    public void setItemCallbackSender(MutableLiveData<MediaEvent> sender) {
        this.mItemCallbackSender = sender;

    }
}
