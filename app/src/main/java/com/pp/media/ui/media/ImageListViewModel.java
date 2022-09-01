package com.pp.media.ui.media;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LifecycleOwner;

import com.pp.media.adapter.MultiltemAdapter;
import com.pp.media.callback.RecycleCallbackHolder;
import com.pp.media.callback.OnBeanListChangedCallBack;
import com.pp.media.callback.OnListHoldCallbackListener;
import com.pp.media.media.Image;
import com.pp.media.media.ImageBucket;
import com.pp.media.ui.media.model.ImageListItemViewModel;
import com.pp.mvvm.base.LifecycleViewModel;

public class ImageListViewModel extends LifecycleViewModel {

    public final ObservableField<String> titleState = new ObservableField<>();
    private ImageBucket mImageBucket;

    public ImageListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
    }

    private final ObservableList<ImageListItemViewModel> mItemList = new ObservableArrayList<>();
    RecycleCallbackHolder<ObservableList.OnListChangedCallback<ObservableList<ImageListItemViewModel>>>
            mCallbackHolder = new RecycleCallbackHolder<ObservableList.OnListChangedCallback<ObservableList<ImageListItemViewModel>>>(new OnListHoldCallbackListener<>(mItemList));


    OnBeanListChangedCallBack.Adapter<ImageListItemViewModel, Image> mOnBeanChangedAdapter
            = new OnBeanListChangedCallBack.Adapter<ImageListItemViewModel, Image>() {
        ImageListItemViewModel itemViewModel;

        @Override
        public ImageListItemViewModel createItem(Image image) {
            itemViewModel = new ImageListItemViewModel();
            itemViewModel.setImage(image);
            return itemViewModel;
        }
    };

    public void setData(ImageBucket imageBucket, MultiltemAdapter<ImageListItemViewModel> adapter) {
        if (null != mImageBucket
                && mImageBucket.getId() == imageBucket.getId()
                && mImageBucket.getDisPlayName().equals(imageBucket.getDisPlayName())) {
            return;
        }

        this.mImageBucket = imageBucket;
        mItemList.clear();

        ObservableList<Image> imageList = new ObservableArrayList<>();
        OnBeanListChangedCallBack<ImageListItemViewModel, Image> changedCallBack = new OnBeanListChangedCallBack<>(mItemList, mOnBeanChangedAdapter);
        imageList.addOnListChangedCallback(changedCallBack);

        imageList.addAll(imageBucket.getImageMap().values());
        imageList.removeOnListChangedCallback(changedCallBack);

        adapter.setNewData(mItemList);
    }

    public void addOnItemChangedCallback(LifecycleOwner owner, ObservableList.OnListChangedCallback<ObservableList<ImageListItemViewModel>> changedCallback) {
        mCallbackHolder.holdCallback(owner, changedCallback);
    }
}
