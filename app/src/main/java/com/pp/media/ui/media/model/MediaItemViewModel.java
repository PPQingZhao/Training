package com.pp.media.ui.media.model;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.pp.media.adapter.BaseAbstractExpandleItem;
import com.pp.media.repository.bean.Media;

public class MediaItemViewModel extends BaseAbstractExpandleItem<String> {
    public static final int ITEMTYPE = 0;

    public final ObservableField<String> src = new ObservableField<>();

    private Media media;

    public void setMedia(@NonNull Media media) {
        this.media = media;

        src.set(media.getThumbnailsPath());
    }

    public Media getMedia() {
        return media;
    }

    @Override
    public int getItemType() {
        return ITEMTYPE;
    }
}
