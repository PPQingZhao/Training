package com.pp.media.ui.media;

import android.app.Application;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.pp.media.R;
import com.pp.media.adapter.MultiltemAdapter;
import com.pp.media.callback.OnBeanListChangedCallBack;
import com.pp.media.lifecycle.LifecycleObjectHolder;
import com.pp.media.lifecycle.OnListCallbackHolderListener;
import com.pp.media.media.Image;
import com.pp.media.media.ImageBucket;
import com.pp.media.ui.media.event.MediaEvent;
import com.pp.media.ui.media.model.ImageListItemViewModel;
import com.pp.media.ui.media.model.ImagePageItemViewModel;
import com.pp.media.ui.media.model.ImagePageListItemViewModel;
import com.pp.mvvm.base.LifecycleViewModel;

import java.util.ArrayList;
import java.util.List;

public class ImageListViewModel extends LifecycleViewModel {

    public final ObservableField<CharSequence> titleState = new ObservableField<>();
    private ImageBucket mImageBucket;
    private MutableLiveData<MediaEvent> mItemSender;
    private ArrayList<ImagePageItemViewModel> mImagePageDatas = new ArrayList<>();
    private ArrayList<ImagePageListItemViewModel> mImagePageListDatas = new ArrayList<>();

    public ImageListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
    }

    private final ObservableList<ImageListItemViewModel> mItemList = new ObservableArrayList<>();


    OnBeanListChangedCallBack.Adapter<ImageListItemViewModel, Image> mOnBeanChangedAdapter
            = new OnBeanListChangedCallBack.Adapter<ImageListItemViewModel, Image>() {
        ImageListItemViewModel itemViewModel;

        @Override
        public ImageListItemViewModel createItem(Image image) {
            itemViewModel = new ImageListItemViewModel();
            itemViewModel.setImage(image);
            itemViewModel.setSender(mItemSender);
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
        mImagePageDatas.clear();
        mImagePageListDatas.clear();

        ObservableList<Image> imageList = new ObservableArrayList<>();
        OnBeanListChangedCallBack<ImageListItemViewModel, Image> changedCallBack = new OnBeanListChangedCallBack<>(mItemList, mOnBeanChangedAdapter);
        imageList.addOnListChangedCallback(changedCallBack);

        imageList.addAll(imageBucket.getImageMap().values());
        imageList.removeOnListChangedCallback(changedCallBack);

        adapter.setNewData(mItemList);

        if (mItemList.size() > 0) {
            setTitle(imageBucket.getDisPlayName(), imageBucket.getImageMap().size());
        }

    }

    private void setTitle(String title, int count) {
        String tagTitle = new StringBuilder()
                .append(title)
                .append("(")
                .append(count)
                .append(")").toString();
        SpannableString spanTitle = new SpannableString(tagTitle);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getApplication().getResources().getColor(R.color.colorImageListText));
        spanTitle.setSpan(colorSpan, title.length(), tagTitle.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

        titleState.set(spanTitle);
    }

    final LifecycleObjectHolder<ObservableList.OnListChangedCallback<ObservableList<ImageListItemViewModel>>> callbackHolder = new LifecycleObjectHolder<>();

    public void addOnItemChangedCallback(LifecycleOwner owner, ObservableList.OnListChangedCallback<ObservableList<ImageListItemViewModel>> changedCallback) {
        callbackHolder.holde(owner, changedCallback, new OnListCallbackHolderListener<>(mItemList));
    }

    public void setItemSender(MutableLiveData<MediaEvent> sender) {
        this.mItemSender = sender;
    }

    public List<ImagePageItemViewModel> getImagePageData() {
        if (mImagePageDatas.size() == 0) {
            if (null != mImageBucket) {
                ImagePageItemViewModel itemViewModel;
                for (Image image : mImageBucket.getImageMap().values()) {
                    itemViewModel = new ImagePageItemViewModel();
                    itemViewModel.setImage(image);
                    mImagePageDatas.add(itemViewModel);
                }
            }
        }
        return mImagePageDatas;
    }

    public List<ImagePageListItemViewModel> getImagePageListData() {
        if (mImagePageListDatas.size() == 0) {
            if (null != mImageBucket) {
                ImagePageListItemViewModel itemViewModel;
                for (Image image : mImageBucket.getImageMap().values()) {
                    itemViewModel = new ImagePageListItemViewModel();
                    itemViewModel.setImage(image);
                    mImagePageListDatas.add(itemViewModel);
                }
            }
        }
        return mImagePageListDatas;
    }

    public int getImagePos(Image image) {
        int pos = new ArrayList<>(mImageBucket.getImageMap().values()).indexOf(image);
        return pos;
    }
}
