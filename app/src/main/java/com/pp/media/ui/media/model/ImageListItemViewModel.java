package com.pp.media.ui.media.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.pp.media.adapter.BaseAbstractExpandleItem;
import com.pp.media.media.Image;
import com.pp.media.repository.bean.Media;

public class ImageListItemViewModel extends BaseAbstractExpandleItem<String> {
    public static final int ITEMTYPE = 0;

    public final ObservableField<String> src = new ObservableField<>();
    private Image image;


    public void setImage(@NonNull Image image) {
        this.image = image;

        String thumbnailpath = image.getThumbnailpath();
        String path = image.getPath();
        src.set(TextUtils.isEmpty(thumbnailpath) ? path : thumbnailpath);
    }

    public Image getImage() {
        return image;
    }

    @Override
    public int getItemType() {
        return ITEMTYPE;
    }
}
