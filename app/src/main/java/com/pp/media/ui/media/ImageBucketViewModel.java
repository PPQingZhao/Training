package com.pp.media.ui.media;

import android.app.Application;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.pp.media.R;
import com.pp.media.adapter.MultiltemAdapter;
import com.pp.media.callback.OnBeanListChangedCallBack;
import com.pp.media.callback.OnBeanListChangedCallBack.Adapter;
import com.pp.media.lifecycle.LifecycleObjectHolder;
import com.pp.media.lifecycle.OnListCallbackHolderListener;
import com.pp.media.media.ImageBucket;
import com.pp.media.repository.MediaRepository;
import com.pp.media.ui.media.event.MediaEvent;
import com.pp.media.ui.media.model.ImageBucketItemViewModel;
import com.pp.mvvm.base.LifecycleViewModel;

import java.util.List;

public class ImageBucketViewModel extends LifecycleViewModel {
    private static final String TAG = "ImageBucketViewModel";
    public final ObservableField<CharSequence> titleState = new ObservableField<>("Image Bucket");
    private MutableLiveData<MediaEvent> mItemCallbackSender;

    public ImageBucketViewModel(@NonNull Application application) {
        super(application);
    }

    private final ObservableList<ImageBucketItemViewModel> mItemList = new ObservableArrayList<>();

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
    }

    LifecycleObjectHolder<ObservableList.OnListChangedCallback<ObservableList<ImageBucketItemViewModel>>> callbackHolder = new LifecycleObjectHolder<>();


    public void addOnItemChangedCallback(LifecycleOwner owner, ObservableList.OnListChangedCallback<ObservableList<ImageBucketItemViewModel>> callback) {
        callbackHolder.holde(owner, callback, new OnListCallbackHolderListener<>(mItemList));
    }

    public void addOnImageBucketChangedCallback(@NonNull MediaRepository repository, LifecycleOwner owner) {
        repository.addOnImageBucketChangedCallback(owner, new OnBeanListChangedCallBack<ImageBucketItemViewModel, ImageBucket>(mItemList, mBucketAdapter) {
            @Override
            public void onItemRangeInserted(ObservableList<ImageBucket> sender, int positionStart, int itemCount) {
                super.onItemRangeInserted(sender, positionStart, itemCount);
                setTitle(sender);
            }
        });
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

        OnBeanListChangedCallBack<ImageBucketItemViewModel, ImageBucket> callBack = new OnBeanListChangedCallBack<ImageBucketItemViewModel, ImageBucket>(mItemList, mBucketAdapter) {
            @Override
            public void onItemRangeInserted(ObservableList<ImageBucket> sender, int positionStart, int itemCount) {
                super.onItemRangeInserted(sender, positionStart, itemCount);
                setTitle(sender);
            }
        };
        imageBuckets.addOnListChangedCallback(callBack);

        imageBuckets.addAll(images);

        imageBuckets.removeOnListChangedCallback(callBack);

        adapter.setNewData(mItemList);

    }

    private void setTitle(List<ImageBucket> imageBuckets) {
        int count = 0;
        for (ImageBucket bucket : imageBuckets) {
            count += bucket.getImageMap().size();
        }

        Log.e(TAG, count + "");
        String title = getApplication().getResources().getString(R.string.title_imagebucket);
        StringBuilder titleBuilder = new StringBuilder(title)
                .append("(")
                .append(count)
                .append(")");

        SpannableString spanTitle = new SpannableString(titleBuilder.toString());

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getApplication().getResources().getColor(R.color.colorImageListText));
        spanTitle.setSpan(colorSpan, title.length(), titleBuilder.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

        titleState.set(spanTitle);
    }

    public void setItemCallbackSender(MutableLiveData<MediaEvent> sender) {
        this.mItemCallbackSender = sender;
    }
}
