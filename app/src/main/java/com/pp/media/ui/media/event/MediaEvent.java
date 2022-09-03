package com.pp.media.ui.media.event;

import com.pp.mvvm.event.ViewEvent;

public class MediaEvent<T> extends ViewEvent<T> {
    public static final String ACTION_SEND_IMAGEBUCKET_FOR_IMAGLIST = "Send imagebucket for imagelist fragment";
    public static final String ACTION_ON_IMAGELIST_BACKPRESSED = "On ImageListFragment backpressed";
    public static final String ACTION_ON_IMAGEDETAIL_BACKPRESSED = "On ImageDetailFragment backpressed";
    public static final String ACTION_ON_IMAGELIST_ITME_CLICK = "Send image for imagedetail fragment";

    public static <Data> MediaEvent<Data> newEvent(Data data, String type) {
        MediaEvent<Data> event = new MediaEvent<>();
        event.setType(type);
        if (null != data) {
            DataOwner<Data> owner = new DataOwner<Data>(data);
            event.setDataOwner(owner);
        }
        return event;
    }

    public static MediaEvent newEvent(String type) {
        return newEvent(null, type);
    }
}
