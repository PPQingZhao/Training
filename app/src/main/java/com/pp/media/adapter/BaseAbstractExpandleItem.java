package com.pp.media.adapter;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

public abstract class BaseAbstractExpandleItem<T> extends AbstractExpandableItem<T> implements MultiItemEntity {

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
