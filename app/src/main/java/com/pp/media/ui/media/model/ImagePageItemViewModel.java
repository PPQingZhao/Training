package com.pp.media.ui.media.model;

import android.text.TextUtils;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.pp.media.adapter.BaseAbstractExpandleItem;
import com.pp.media.media.Image;

import java.text.SimpleDateFormat;
import java.util.AbstractSequentialList;
import java.util.Date;
import java.util.ListIterator;

public class ImagePageItemViewModel extends BaseAbstractExpandleItem {

    public static final int ITEM_TYPE = 0;
    public ObservableField<String> imagePath = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> hour = new ObservableField<>();

    public void setImage(Image image) {
        String thumbnailpath = image.getThumbnailpath();
        String path = image.getPath();
        imagePath.set(TextUtils.isEmpty(thumbnailpath) ? path : thumbnailpath);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
        Date date = new Date(image.getAddDateTimeMill());
        String dateStr = dateFormat.format(date);
        String hourStr = hourFormat.format(date);

        this.date.set(dateStr);
        this.hour.set(hourStr);

    }

    @Override
    public int getItemType() {
        return ITEM_TYPE;
    }
}
