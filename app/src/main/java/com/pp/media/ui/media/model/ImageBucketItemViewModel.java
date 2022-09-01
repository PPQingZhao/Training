package com.pp.media.ui.media.model;

import android.text.TextUtils;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.pp.media.adapter.BaseAbstractExpandleItem;
import com.pp.media.media.Image;
import com.pp.media.media.ImageBucket;
import com.pp.media.ui.event.MediaEvent;
import com.pp.media.ui.event.SharedEvent;
import com.pp.mvvm.event.ViewEvent;

public class ImageBucketItemViewModel extends BaseAbstractExpandleItem<String> {
    public static final int ITEMTYPE = 0;

    public final ObservableField<String> disPlayName = new ObservableField<>();
    public final ObservableField<String> coverImage = new ObservableField<>();
    public final ObservableField<String> imageCount = new ObservableField<>();

    private ImageBucket imageBucket;
    private MutableLiveData<MediaEvent> mSender;

    public ImageBucket getImageBucket() {
        return imageBucket;
    }

    public void setImageBucket(ImageBucket imageBucket) {
        this.imageBucket = imageBucket;
        if (null != imageBucket) {
            disPlayName.set(imageBucket.getDisPlayName());
        }

        imageCount.set(String.valueOf(imageBucket.getImageMap().size()));
        try {
            Image image = imageBucket.getImageMap().valueAt(0);
            String thumbnailpath = image.getThumbnailpath();
            String path = image.getPath();

            coverImage.set(TextUtils.isEmpty(thumbnailpath) ? path : thumbnailpath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onItemClick(View view) {
        MediaEvent<ImageBucket> event = MediaEvent.newEvent(imageBucket, MediaEvent.ACTION_SEND_IMAGEBUCKET_FOR_IMAGLIST);
        mSender.setValue(event);
    }

    @Override
    public int getItemType() {
        return ITEMTYPE;
    }

    public void setCallbackSender(MutableLiveData<MediaEvent> sender) {
        this.mSender = sender;
    }
}
