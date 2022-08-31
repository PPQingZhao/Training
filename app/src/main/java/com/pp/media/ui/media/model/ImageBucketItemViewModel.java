package com.pp.media.ui.media.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.pp.media.adapter.BaseAbstractExpandleItem;
import com.pp.media.media.Image;
import com.pp.media.media.ImageBucket;
import com.pp.media.repository.bean.Media;

import java.util.Collection;

public class ImageBucketItemViewModel extends BaseAbstractExpandleItem<String> {
    public static final int ITEMTYPE = 0;

    public final ObservableField<String> disPlayName = new ObservableField<>();
    public final ObservableField<String> coverImage = new ObservableField<>();
    public final ObservableField<String> imageCount = new ObservableField<>();

    private ImageBucket imageBucket;

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

    @Override
    public int getItemType() {
        return ITEMTYPE;
    }
}
