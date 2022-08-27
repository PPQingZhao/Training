package com.pp.media.ui.media.model;

import androidx.databinding.ObservableField;

import com.pp.media.adapter.BaseAbstractExpandleItem;

public class MediaItemViewModel extends BaseAbstractExpandleItem<String> {
    public static final int ITEMTYPE = 0;

    public final ObservableField<String> src = new ObservableField<>();
    public final ObservableField<String> date = new ObservableField<>();

    @Override
    public int getItemType() {
        return ITEMTYPE;
    }
}
