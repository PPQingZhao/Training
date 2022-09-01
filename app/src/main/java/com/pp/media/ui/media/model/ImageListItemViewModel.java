package com.pp.media.ui.media.model;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.pp.media.adapter.BaseAbstractExpandleItem;
import com.pp.media.media.Image;
import com.pp.media.ui.event.MediaEvent;

public class ImageListItemViewModel extends BaseAbstractExpandleItem<String> {
    public static final int ITEMTYPE = 0;

    public final ObservableField<String> src = new ObservableField<>();
    private Image image;
    private MutableLiveData<MediaEvent> mSender;


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

    public void setSender(MutableLiveData<MediaEvent> sender) {
        this.mSender = sender;
    }

    public void onItemClick(View v){
        mSender.setValue(MediaEvent.newEvent(image,MediaEvent.ACTION_SEND_IMAGE_FOR_IMAGDETAIL));
    }
}
