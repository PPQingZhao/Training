package com.pp.media.ui.media.model;

import android.text.TextUtils;

import androidx.databinding.ObservableField;

import com.pp.media.adapter.BaseAbstractExpandleItem;
import com.pp.media.media.Image;

public class ImagePageListItemViewModel extends BaseAbstractExpandleItem {

    public static final int ITEM_TYPE = 0;
    public ObservableField<String> imagePath = new ObservableField<>();
    public ObservableField<Boolean> selected = new ObservableField<>();

    public void setImage(Image image) {
        String thumbnailpath = image.getThumbnailpath();
        String path = image.getPath();
        imagePath.set(TextUtils.isEmpty(thumbnailpath) ? path : thumbnailpath);

    }

    @Override
    public int getItemType() {
        return ITEM_TYPE;
    }
}
